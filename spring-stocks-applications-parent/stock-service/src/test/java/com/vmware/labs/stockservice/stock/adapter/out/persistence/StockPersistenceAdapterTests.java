package com.vmware.labs.stockservice.stock.adapter.out.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vmware.labs.components.autoconfigure.UuidGenerator;
import com.vmware.labs.stockservice.stock.domain.StockProjection;
import com.vmware.labs.stockservice.stock.domain.events.PriceChanged;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class StockPersistenceAdapterTests {

    StockProjectionPersistenceAdapter subject;

    StockDomainEventRepository mockStockDomainEventRepository;
    StockProjectionRepository mockStockProjectionRepository;
    UuidGenerator mockUuidGenerator;

    UUID fakeId = UUID.randomUUID();
    Long fakeVersion = null;
    String fakeSymbol = "fakeSymbol";
    BigDecimal fakePrice = new BigDecimal( "1.00" );
    Instant fakeOccurredOn = Instant.now();

    @BeforeEach
    public void setup() {

        this.mockStockDomainEventRepository = mock( StockDomainEventRepository.class );
        this.mockStockProjectionRepository = mock( StockProjectionRepository.class );
        this.mockUuidGenerator = mock( UuidGenerator.class );

        this.subject = new StockProjectionPersistenceAdapter( this.mockStockDomainEventRepository, this.mockStockProjectionRepository, this.mockUuidGenerator );

        when( this.mockUuidGenerator.generate() ).thenReturn( fakeId );

    }

    @Test
    void testGetStockEvents() throws JsonProcessingException {

        var fakeEvent = new PriceChanged( fakeSymbol, fakePrice, fakeOccurredOn );

        var fakeDomainEventEntity = new DomainEventEntity( fakeId, fakeVersion, fakeSymbol, fakeOccurredOn, fakeEvent );
        when( this.mockStockDomainEventRepository.findAllBySymbol( fakeSymbol ) ).thenReturn( Flux.just( fakeDomainEventEntity ) );

        var expected = new PriceChanged( fakeSymbol, fakePrice, fakeOccurredOn );

        StepVerifier.create( this.subject.getStockEvents( fakeSymbol ) )
                .consumeNextWith( domainEvent -> assertThat( domainEvent ).isEqualTo( expected ) )
                .expectComplete()
                .verify();

        verify( this.mockStockDomainEventRepository ).findAllBySymbol( fakeSymbol );
        verifyNoMoreInteractions( this.mockStockDomainEventRepository );
        verifyNoInteractions( this.mockUuidGenerator );

    }

    @Test
    void testSave() throws JsonProcessingException {

        var fakePriceChangedEvent = new PriceChanged( fakeSymbol, fakePrice, fakeOccurredOn );

        var fakeDomainEventEntity = new DomainEventEntity( fakeId, fakeVersion, fakeSymbol, fakeOccurredOn, fakePriceChangedEvent );
        when( this.mockStockDomainEventRepository.save( fakeDomainEventEntity ) ).thenReturn( Mono.just( fakeDomainEventEntity ) );

        StepVerifier.create( this.subject.save( fakePriceChangedEvent ) )
                .consumeNextWith( savedEvent -> assertThat( savedEvent ).isEqualTo( fakePriceChangedEvent ) )
                .expectComplete()
                .verify();

        DomainEventEntity expectedDomainEventEntity = new DomainEventEntity( fakeId, fakeVersion, fakeSymbol, fakeOccurredOn, fakePriceChangedEvent );

        verify( this.mockStockDomainEventRepository ).save( expectedDomainEventEntity );
        verify( this.mockUuidGenerator ).generate();
        verifyNoMoreInteractions( this.mockStockDomainEventRepository, this.mockUuidGenerator );

    }

    @Test
    void testCacheStock() {

        when( this.mockUuidGenerator.generate() ).thenReturn( fakeId );

        var fakeEntity = new StockProjectionEntity( fakeId, null, fakeSymbol, fakePrice, fakeOccurredOn );
        when( this.mockStockProjectionRepository.findBySymbol( fakeSymbol ) ).thenReturn( Mono.empty() );
        when( this.mockStockProjectionRepository.save( fakeEntity ) ).thenReturn( Mono.just( fakeEntity ) );

        var expected = new StockProjection( fakeSymbol, fakePrice, fakeOccurredOn );
        StepVerifier.create( this.subject.updateProjection( new StockProjection( fakeSymbol, fakePrice, fakeOccurredOn ) ) )
                .consumeNextWith( actual -> assertThat( actual ).isEqualTo( expected ))
                .expectComplete()
                .verify();

        var expectedEntity = new StockProjectionEntity( fakeId, null, fakeSymbol, fakePrice, fakeOccurredOn );

        verify( this.mockUuidGenerator ).generate();
        verify( this.mockStockProjectionRepository ).findBySymbol( fakeSymbol );
        verify( this.mockStockProjectionRepository ).save( expectedEntity );
        verifyNoMoreInteractions( this.mockStockProjectionRepository, this.mockUuidGenerator );
        verifyNoInteractions( this.mockStockDomainEventRepository );

    }

    @Test
    void testCacheExistingStock() {

        when( this.mockUuidGenerator.generate() ).thenReturn( fakeId );

        var fakeEntity = new StockProjectionEntity( fakeId, 0L, fakeSymbol, fakePrice, fakeOccurredOn );
        when( this.mockStockProjectionRepository.findBySymbol( fakeSymbol ) ).thenReturn( Mono.just( fakeEntity ) );

        var updatedFakeEntity = new StockProjectionEntity( fakeId, 1L, fakeSymbol, fakePrice, fakeOccurredOn );
        when( this.mockStockProjectionRepository.save( fakeEntity ) ).thenReturn( Mono.just( updatedFakeEntity ) );

        var expected = new StockProjection( fakeSymbol, fakePrice, fakeOccurredOn );
        StepVerifier.create( this.subject.updateProjection( new StockProjection( fakeSymbol, fakePrice, fakeOccurredOn ) ) )
                .consumeNextWith( actual -> assertThat( actual ).isEqualTo( expected ))
                .expectComplete()
                .verify();

        var expectedEntity = new StockProjectionEntity( fakeId, 0L, fakeSymbol, fakePrice, fakeOccurredOn );

        verify( this.mockUuidGenerator ).generate();
        verify( this.mockStockProjectionRepository ).findBySymbol( fakeSymbol );
        verify( this.mockStockProjectionRepository ).save( expectedEntity );
        verifyNoMoreInteractions( this.mockStockProjectionRepository, this.mockUuidGenerator );
        verifyNoInteractions( this.mockStockDomainEventRepository );

    }

    @Test
    void testLookupBySymbol() {

        var fakeStockProjection = new StockProjectionEntity( fakeId, null, fakeSymbol, fakePrice, fakeOccurredOn );
        when( this.mockStockProjectionRepository.findBySymbol( fakeSymbol) ).thenReturn( Mono.just( fakeStockProjection ) );

        var expected = new StockProjection( fakeSymbol, fakePrice, fakeOccurredOn );
        StepVerifier.create( this.subject.lookupBySymbol( fakeSymbol ) )
                .consumeNextWith( actual -> assertThat( actual ).isEqualTo( expected ) )
                .expectComplete()
                .verify();

        verify( this.mockStockProjectionRepository ).findBySymbol( fakeSymbol );
        verifyNoMoreInteractions( this.mockStockProjectionRepository );
        verifyNoInteractions( this.mockStockDomainEventRepository, this.mockUuidGenerator );

    }

}

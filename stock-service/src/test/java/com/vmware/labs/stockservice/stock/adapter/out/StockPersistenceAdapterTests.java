package com.vmware.labs.stockservice.stock.adapter.out;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vmware.labs.stockservice.common.usecase.UuidGenerator;
import com.vmware.labs.stockservice.stock.domain.events.PriceChanged;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class StockPersistenceAdapterTests {

    StockPersistenceAdapter subject;

    ObjectMapper mapper;

    StockEventsDataStore mockStockEventsDataStore;
    StockCacheDataStore mockStockCacheDataStore;
    UuidGenerator mockUuidGenerator;

    UUID fakeId = UUID.randomUUID();
    String fakeSymbol = "fakeSymbol";
    BigDecimal fakePrice = new BigDecimal( "1.00" );
    Instant fakeOccurredOn = Instant.now();

    @BeforeEach
    public void setup() {

        this.mapper =
                new ObjectMapper()
                        .findAndRegisterModules()
                        .registerModule( new JavaTimeModule() )
                        .disable( WRITE_DATES_AS_TIMESTAMPS );

        this.mockStockEventsDataStore = mock( StockEventsDataStore.class );
        this.mockStockCacheDataStore = mock( StockCacheDataStore.class );
        this.mockUuidGenerator = mock( UuidGenerator.class );

        this.subject = new StockPersistenceAdapter( this.mockStockEventsDataStore, this.mockStockCacheDataStore, this.mockUuidGenerator, this.mapper );

        when( this.mockUuidGenerator.generate() ).thenReturn( fakeId );

    }

    @Test
    void testGetStockEvents() throws JsonProcessingException {

        var fakeJson = this.mapper.writeValueAsString( new PriceChanged( fakeSymbol, fakePrice, fakeOccurredOn ) );

        var fakeDomainEventEntity = new DomainEventEntity( fakeId, fakeSymbol, fakeOccurredOn, fakeJson );
        when( this.mockStockEventsDataStore.findBySymbol( fakeSymbol ) ).thenReturn( Flux.just( fakeDomainEventEntity ) );

        var expected = new PriceChanged( fakeSymbol, fakePrice, fakeOccurredOn );

        StepVerifier.create( this.subject.getStockEvents( fakeSymbol ) )
                .consumeNextWith( domainEvent -> assertThat( domainEvent ).isEqualTo( expected ) )
                .expectComplete()
                .verify();

        verify( this.mockStockEventsDataStore).findBySymbol( fakeSymbol );
        verifyNoMoreInteractions( this.mockStockEventsDataStore);
        verifyNoInteractions( this.mockUuidGenerator );

    }

    @Test
    void testSave() throws JsonProcessingException {

        var fakePriceChangedEvent = new PriceChanged( fakeSymbol, fakePrice, fakeOccurredOn );
        String fakeJson = this.mapper.writeValueAsString( fakePriceChangedEvent );

        var fakeDomainEventEntity = new DomainEventEntity( fakeId, fakeSymbol, fakeOccurredOn, fakeJson );
        when( this.mockStockEventsDataStore.save( fakeDomainEventEntity ) ).thenReturn( Mono.just( fakeDomainEventEntity ) );

        StepVerifier.create( this.subject.save( fakePriceChangedEvent ) )
                .consumeNextWith( savedEvent -> assertThat( savedEvent ).isEqualTo( fakePriceChangedEvent ) )
                .expectComplete()
                .verify();

        DomainEventEntity expectedDomainEventEntity = new DomainEventEntity( fakeId, fakeSymbol, fakeOccurredOn, fakeJson );

        verify( this.mockStockEventsDataStore).save( expectedDomainEventEntity );
        verify( this.mockUuidGenerator ).generate();
        verifyNoMoreInteractions( this.mockStockEventsDataStore, this.mockUuidGenerator );

    }

}
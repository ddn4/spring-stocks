package com.vmware.labs.stockService.stock.adapter.out;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vmware.labs.stockService.common.useCase.UuidGenerator;
import com.vmware.labs.stockService.stock.domain.events.DomainEvent;
import com.vmware.labs.stockService.stock.domain.events.PriceChanged;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static java.util.List.of;
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
    public void testGetStockEvents() throws JsonProcessingException {

        String fakeJson = this.mapper.writeValueAsString( new PriceChanged( fakeSymbol, fakePrice, fakeOccurredOn ) );

        DomainEventEntity fakeDomainEventEntity = new DomainEventEntity( fakeId, fakeSymbol, fakeOccurredOn, fakeJson );
        when( this.mockStockEventsDataStore.findBySymbol( fakeSymbol ) ).thenReturn( of( fakeDomainEventEntity ) );

        List<DomainEvent> actual = this.subject.getStockEvents( fakeSymbol );

        List<DomainEvent> expected = of( new PriceChanged( fakeSymbol, fakePrice, fakeOccurredOn ) );
        assertThat( actual ).isEqualTo( expected );

        verify( this.mockStockEventsDataStore).findBySymbol( fakeSymbol );
        verifyNoMoreInteractions( this.mockStockEventsDataStore);
        verifyNoInteractions( this.mockUuidGenerator );

    }

    @Test
    public void testSave() throws JsonProcessingException {

        PriceChanged fakePriceChangedEvent = new PriceChanged( fakeSymbol, fakePrice, fakeOccurredOn );

        this.subject.save( fakePriceChangedEvent );

        String fakeJson = this.mapper.writeValueAsString( fakePriceChangedEvent );
        DomainEventEntity expectedDomainEventEntity = new DomainEventEntity( fakeId, fakeSymbol, fakeOccurredOn, fakeJson );

        verify( this.mockStockEventsDataStore).save( expectedDomainEventEntity );
        verify( this.mockUuidGenerator ).generate();
        verifyNoMoreInteractions( this.mockStockEventsDataStore, this.mockUuidGenerator );

    }

}
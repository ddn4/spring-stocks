package com.vmware.labs.stockService.stock.application;

import com.vmware.labs.stockService.stock.application.in.CacheStockUseCase.CacheStockCommand;
import com.vmware.labs.stockService.stock.application.out.GetStockEventsPort;
import com.vmware.labs.stockService.stock.application.out.PutStockCachePort;
import com.vmware.labs.stockService.stock.domain.StockCache;
import com.vmware.labs.stockService.stock.domain.events.DomainEvent;
import com.vmware.labs.stockService.stock.domain.events.PriceChanged;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static java.util.List.of;
import static org.mockito.Mockito.*;

class CacheStockServiceTests {

    CacheStockService subject;

    GetStockEventsPort mockGetStockEventsPort;
    PutStockCachePort mockPutStockCachePort;

    String fakeSymbol = "fakeSymbol";
    BigDecimal fakePrice = new BigDecimal( "1.00" );
    Instant fakeOccurredOn = Instant.now();

    @BeforeEach
    public void setup() {

        this.mockGetStockEventsPort = mock( GetStockEventsPort.class );
        this.mockPutStockCachePort = mock( PutStockCachePort.class );

        this.subject = new CacheStockService( this.mockGetStockEventsPort, this.mockPutStockCachePort );

    }

    @Test
    public void testExecute() {

        DomainEvent fakeDomainEvent = new PriceChanged( fakeSymbol, fakePrice, fakeOccurredOn );
        when( this.mockGetStockEventsPort.getStockEvents( fakeSymbol ) ).thenReturn( of( fakeDomainEvent ) );

        CacheStockCommand fakeCommand = new CacheStockCommand( fakeSymbol );
        this.subject.execute( fakeCommand );

        StockCache expectedStockCache = new StockCache( fakeSymbol, fakePrice, fakeOccurredOn );

        verify( this.mockGetStockEventsPort ).getStockEvents( fakeSymbol );
        verify( this.mockPutStockCachePort ).cacheStock( expectedStockCache );
        verifyNoMoreInteractions( this.mockGetStockEventsPort, this.mockPutStockCachePort );

    }

}
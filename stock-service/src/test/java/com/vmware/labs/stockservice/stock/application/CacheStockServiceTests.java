package com.vmware.labs.stockservice.stock.application;

import com.vmware.labs.stockservice.stock.application.in.CacheStockUseCase.CacheStockCommand;
import com.vmware.labs.stockservice.stock.application.out.GetStockEventsPort;
import com.vmware.labs.stockservice.stock.application.out.PutStockCachePort;
import com.vmware.labs.stockservice.stock.domain.Stock;
import com.vmware.labs.stockservice.stock.domain.StockCache;
import com.vmware.labs.stockservice.stock.domain.events.DomainEvent;
import com.vmware.labs.stockservice.stock.domain.events.PriceChanged;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
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
    void testExecute() {

        DomainEvent fakeDomainEvent = new PriceChanged( fakeSymbol, fakePrice, fakeOccurredOn );
        when( this.mockGetStockEventsPort.getStockEvents( fakeSymbol ) ).thenReturn( Flux.just( fakeDomainEvent ) );

        Stock expected = new Stock( fakeSymbol );

        StepVerifier.create( this.subject.execute( new CacheStockCommand( fakeSymbol ) ) )
                .consumeNextWith( cachedStock -> assertThat( cachedStock ).isEqualTo( expected ) )
                .expectComplete()
                .verify();

        StockCache expectedStockCache = new StockCache( fakeSymbol, fakePrice, fakeOccurredOn );

        verify( this.mockGetStockEventsPort ).getStockEvents( fakeSymbol );
        verify( this.mockPutStockCachePort ).cacheStock( expectedStockCache );
        verifyNoMoreInteractions( this.mockGetStockEventsPort, this.mockPutStockCachePort );

    }

}
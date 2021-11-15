package com.vmware.labs.stockservice.stock.application;

import com.vmware.labs.stockservice.stock.application.in.CacheStockUseCase.CacheStockCommand;
import com.vmware.labs.stockservice.stock.application.out.GetStockEventsPort;
import com.vmware.labs.stockservice.stock.application.out.UpdateStockProjectionPort;
import com.vmware.labs.stockservice.stock.domain.Stock;
import com.vmware.labs.stockservice.stock.domain.StockProjection;
import com.vmware.labs.stockservice.stock.domain.events.PriceChanged;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CacheStockServiceTests {

    CacheStockService subject;

    GetStockEventsPort mockGetStockEventsPort;
    UpdateStockProjectionPort mockUpdateStockProjectionPort;

    String fakeSymbol = "fakeSymbol";
    BigDecimal fakePrice = new BigDecimal( "1.00" );
    Instant fakeOccurredOn = Instant.now();

    @BeforeEach
    public void setup() {

        this.mockGetStockEventsPort = mock( GetStockEventsPort.class );
        this.mockUpdateStockProjectionPort = mock( UpdateStockProjectionPort.class );

        this.subject = new CacheStockService( this.mockGetStockEventsPort, this.mockUpdateStockProjectionPort);

    }

    @Test
    void testExecute() {

        var fakeDomainEvent = new PriceChanged( fakeSymbol, fakePrice, fakeOccurredOn );
        when( this.mockGetStockEventsPort.getStockEvents( fakeSymbol ) ).thenReturn( Flux.just( fakeDomainEvent ) );

        var fakeStockProjection = new StockProjection( fakeSymbol, fakePrice, fakeOccurredOn );
        when( this.mockUpdateStockProjectionPort.updateProjection( fakeStockProjection ) ).thenReturn( Mono.just( fakeStockProjection ) );

        Stock expected = new Stock( fakeSymbol );

        StepVerifier.create( this.subject.execute( new CacheStockCommand( fakeSymbol ) ) )
                .consumeNextWith( cachedStock -> assertThat( cachedStock ).isEqualTo( expected ) )
                .expectComplete()
                .verify();

        StockProjection expectedStockProjection = new StockProjection( fakeSymbol, fakePrice, fakeOccurredOn );

        verify( this.mockGetStockEventsPort ).getStockEvents( fakeSymbol );
        verify( this.mockUpdateStockProjectionPort).updateProjection( expectedStockProjection );
        verifyNoMoreInteractions( this.mockGetStockEventsPort, this.mockUpdateStockProjectionPort);

    }

}
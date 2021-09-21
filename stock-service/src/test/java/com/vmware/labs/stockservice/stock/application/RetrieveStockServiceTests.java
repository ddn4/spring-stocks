package com.vmware.labs.stockservice.stock.application;

import com.vmware.labs.stockservice.stock.application.in.RetrieveStockUseCase.RetrieveStockCommand;
import com.vmware.labs.stockservice.stock.application.in.RetrieveStockUseCase.StockNotFoundException;
import com.vmware.labs.stockservice.stock.application.out.LookupStockPort;
import com.vmware.labs.stockservice.stock.domain.StockCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Instant;

import static org.mockito.Mockito.*;

class RetrieveStockServiceTests {

    RetrieveStockService subject;

    LookupStockPort mockLookupStockPort;

    String fakeSymbol = "fakeSymbol";
    BigDecimal fakePrice = new BigDecimal( "1.00" );
    Instant fakeOccurredOn = Instant.now();

    @BeforeEach
    public void setup() {

        this.mockLookupStockPort = mock( LookupStockPort.class );

        this.subject = new RetrieveStockService( this.mockLookupStockPort );

    }

    @Test
    void testExecute() {

        when( this.mockLookupStockPort.lookupBySymbol( fakeSymbol ) ).thenReturn( Mono.just( new StockCache( fakeSymbol, fakePrice, fakeOccurredOn ) ) );

        StepVerifier.create( this.subject.execute( new RetrieveStockCommand( fakeSymbol ) ) )
                .expectNext( new StockCache( fakeSymbol, fakePrice, fakeOccurredOn ) )
                .verifyComplete();

        verify( this.mockLookupStockPort ).lookupBySymbol( fakeSymbol );
        verifyNoMoreInteractions( this.mockLookupStockPort );

    }

    @Test
    void testExecute_verifyStockNotFoundException() {

        when( this.mockLookupStockPort.lookupBySymbol( fakeSymbol ) ).thenReturn( Mono.empty() );

        StepVerifier.create( this.subject.execute( new RetrieveStockCommand( fakeSymbol ) ) )
                .expectError( StockNotFoundException.class )
                .verify();

        verify( this.mockLookupStockPort ).lookupBySymbol( fakeSymbol );
        verifyNoMoreInteractions( this.mockLookupStockPort );

    }

}
package com.vmware.labs.stockservice.stock.application;

import com.vmware.labs.stockservice.stock.application.in.RetrieveStockUseCase.RetrieveStockCommand;
import com.vmware.labs.stockservice.stock.application.in.RetrieveStockUseCase.StockNotFoundException;
import com.vmware.labs.stockservice.stock.application.out.LookupStockProjectionPort;
import com.vmware.labs.stockservice.stock.domain.StockProjection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Instant;

import static org.mockito.Mockito.*;

class RetrieveStockServiceTests {

    RetrieveStockService subject;

    LookupStockProjectionPort mockLookupStockProjectionPort;

    String fakeSymbol = "fakeSymbol";
    BigDecimal fakePrice = new BigDecimal( "1.00" );
    Instant fakeOccurredOn = Instant.now();

    @BeforeEach
    public void setup() {

        this.mockLookupStockProjectionPort = mock( LookupStockProjectionPort.class );

        this.subject = new RetrieveStockService( this.mockLookupStockProjectionPort);

    }

    @Test
    void testExecute() {

        when( this.mockLookupStockProjectionPort.lookupBySymbol( fakeSymbol ) ).thenReturn( Mono.just( new StockProjection( fakeSymbol, fakePrice, fakeOccurredOn ) ) );

        StepVerifier.create( this.subject.execute( new RetrieveStockCommand( fakeSymbol ) ) )
                .expectNext( new StockProjection( fakeSymbol, fakePrice, fakeOccurredOn ) )
                .verifyComplete();

        verify( this.mockLookupStockProjectionPort).lookupBySymbol( fakeSymbol );
        verifyNoMoreInteractions( this.mockLookupStockProjectionPort);

    }

    @Test
    void testExecute_verifyStockNotFoundException() {

        when( this.mockLookupStockProjectionPort.lookupBySymbol( fakeSymbol ) ).thenReturn( Mono.empty() );

        StepVerifier.create( this.subject.execute( new RetrieveStockCommand( fakeSymbol ) ) )
                .expectError( StockNotFoundException.class )
                .verify();

        verify( this.mockLookupStockProjectionPort).lookupBySymbol( fakeSymbol );
        verifyNoMoreInteractions( this.mockLookupStockProjectionPort);

    }

}
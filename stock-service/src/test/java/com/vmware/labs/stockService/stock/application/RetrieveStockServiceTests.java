package com.vmware.labs.stockService.stock.application;

import com.vmware.labs.stockService.stock.application.in.RetrieveStockUseCase.RetrieveStockCommand;
import com.vmware.labs.stockService.stock.application.in.RetrieveStockUseCase.StockNotFoundException;
import com.vmware.labs.stockService.stock.application.out.LookupStockPort;
import com.vmware.labs.stockService.stock.application.out.StockExistsPort;
import com.vmware.labs.stockService.stock.domain.StockCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Instant;

import static org.mockito.Mockito.*;

class RetrieveStockServiceTests {

    RetrieveStockService subject;

    StockExistsPort mockStockExistsPort;
    LookupStockPort mockLookupStockPort;

    String fakeSymbol = "fakeSymbol";
    BigDecimal fakePrice = new BigDecimal( "1.00" );
    Instant fakeOccurredOn = Instant.now();

    @BeforeEach
    public void setup() {

        this.mockStockExistsPort = mock( StockExistsPort.class );
        this.mockLookupStockPort = mock( LookupStockPort.class );

        this.subject = new RetrieveStockService( this.mockStockExistsPort, this.mockLookupStockPort );

    }

    @Test
    public void testExecute() {

        StockCache fakeStockCache = new StockCache( fakeSymbol, fakePrice, fakeOccurredOn );

        when( this.mockStockExistsPort.exists( fakeSymbol ) ).thenReturn( Boolean.TRUE );
        when( this.mockLookupStockPort.lookupBySymbol( fakeSymbol ) ).thenReturn( fakeStockCache );

        RetrieveStockCommand fakeCommand = new RetrieveStockCommand( fakeSymbol );

        StepVerifier.create( this.subject.execute( fakeCommand ) )
                .expectNext( new StockCache( fakeSymbol, fakePrice, fakeOccurredOn ) )
                .verifyComplete();

        verify( this.mockStockExistsPort ).exists( fakeSymbol );
        verify( this.mockLookupStockPort ).lookupBySymbol( fakeSymbol );
        verifyNoMoreInteractions( this.mockStockExistsPort, this.mockLookupStockPort );

    }

    @Test
    public void testExecute_verifyStockNotFoundException() {

        when( this.mockStockExistsPort.exists( fakeSymbol ) ).thenReturn( Boolean.FALSE );

        RetrieveStockCommand fakeCommand = new RetrieveStockCommand( fakeSymbol );

        StepVerifier.create( this.subject.execute( fakeCommand ) )
                .expectError( StockNotFoundException.class )
                .verify();

        verify( this.mockStockExistsPort ).exists( fakeSymbol );
        verifyNoMoreInteractions( this.mockStockExistsPort );
        verifyNoInteractions( this.mockLookupStockPort );

    }

}
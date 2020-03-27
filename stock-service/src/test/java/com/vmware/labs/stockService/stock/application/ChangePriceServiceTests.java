package com.vmware.labs.stockService.stock.application;

import com.vmware.labs.stockService.common.useCase.TimestampGenerator;
import com.vmware.labs.stockService.stock.application.in.ChangePriceUseCase.ChangePriceCommand;
import com.vmware.labs.stockService.stock.application.out.GetStockEventsPort;
import com.vmware.labs.stockService.stock.application.out.PersistStockEventPort;
import com.vmware.labs.stockService.stock.domain.events.PriceChanged;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static java.util.Collections.emptyList;
import static org.mockito.Mockito.*;

class ChangePriceServiceTests {

    ChangePriceService subject;
    GetStockEventsPort mockGetStockEventsPort;
    PersistStockEventPort mockPersistStockEventPort;
    TimestampGenerator mockTimestampGenerator;

    String fakeSymbol = "fakeSymbol";
    BigDecimal fakePrice = new BigDecimal( "1.00" );
    Instant fakeOccurredOn = Instant.now();

    @BeforeEach
    public void setup() {

        this.mockGetStockEventsPort = mock( GetStockEventsPort.class );
        this.mockPersistStockEventPort = mock( PersistStockEventPort.class );
        this.mockTimestampGenerator = mock( TimestampGenerator.class );

        this.subject = new ChangePriceService( this.mockGetStockEventsPort, this.mockPersistStockEventPort, this.mockTimestampGenerator );

    }

    @Test
    public void testExecute() {

        when( this.mockGetStockEventsPort.getStockEvents( fakeSymbol ) ).thenReturn( emptyList() );
        when( this.mockTimestampGenerator.generate() ).thenReturn( fakeOccurredOn );

        ChangePriceCommand fakeCommand = new ChangePriceCommand( fakeSymbol, fakePrice );
        this.subject.execute( fakeCommand );

        PriceChanged expectedEvent = new PriceChanged( fakeSymbol, fakePrice, fakeOccurredOn );

        verify( this.mockGetStockEventsPort ).getStockEvents( fakeSymbol );
        verify( this.mockPersistStockEventPort ).save( expectedEvent );
        verify( this.mockTimestampGenerator ).generate();
        verifyNoMoreInteractions( this.mockGetStockEventsPort, this.mockPersistStockEventPort, this.mockTimestampGenerator );

    }

}
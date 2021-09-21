package com.vmware.labs.stockservice.stock.application;

import com.vmware.labs.stockservice.common.usecase.TimestampGenerator;
import com.vmware.labs.stockservice.stock.application.in.ChangePriceUseCase.ChangePriceCommand;
import com.vmware.labs.stockservice.stock.application.out.GetStockEventsPort;
import com.vmware.labs.stockservice.stock.application.out.PersistStockEventPort;
import com.vmware.labs.stockservice.stock.domain.Stock;
import com.vmware.labs.stockservice.stock.domain.events.PriceChanged;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Instant;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ChangePriceServiceTests {

    ChangePriceService subject;
    GetStockEventsPort mockGetStockEventsPort;
    PersistStockEventPort mockPersistStockEventPort;
    TimestampGenerator mockTimestampGenerator;
    ApplicationEventPublisher mockApplicationEventPublisher;

    String fakeSymbol = "fakeSymbol";
    BigDecimal fakePrice = new BigDecimal( "1.00" );
    Instant fakeOccurredOn = Instant.now();

    @BeforeEach
    public void setup() {

        this.mockGetStockEventsPort = mock( GetStockEventsPort.class );
        this.mockPersistStockEventPort = mock( PersistStockEventPort.class );
        this.mockTimestampGenerator = mock( TimestampGenerator.class );
        this.mockApplicationEventPublisher = mock( ApplicationEventPublisher.class );

        this.subject = new ChangePriceService( this.mockGetStockEventsPort, this.mockPersistStockEventPort, this.mockTimestampGenerator, this.mockApplicationEventPublisher );

    }

    @Test
    void testExecute() {

        when( this.mockGetStockEventsPort.getStockEvents( fakeSymbol ) ).thenReturn( Flux.empty() );
        when( this.mockTimestampGenerator.generate() ).thenReturn( fakeOccurredOn );

        var expected = Stock.createFrom( fakeSymbol, of( new PriceChanged( fakeSymbol, fakePrice, fakeOccurredOn ) ) );

        StepVerifier.create( this.subject.execute( new ChangePriceCommand( fakeSymbol, fakePrice ) ) )
                .consumeNextWith( updatedStock -> assertThat( updatedStock ).isEqualTo( expected ) )
                .expectComplete()
                .verify();

        PriceChanged expectedEvent = new PriceChanged( fakeSymbol, fakePrice, fakeOccurredOn );

        verify( this.mockGetStockEventsPort ).getStockEvents( fakeSymbol );
        verify( this.mockPersistStockEventPort ).save( expectedEvent );
        verify( this.mockTimestampGenerator ).generate();
        verifyNoMoreInteractions( this.mockGetStockEventsPort, this.mockPersistStockEventPort, this.mockTimestampGenerator );

    }

}
package com.vmware.labs.stockservice.stock.adapter.in.integration;

import com.vmware.labs.stockservice.applicationevents.StockUpdateEvent;
import com.vmware.labs.stockservice.stock.application.in.ChangePriceUseCase;
import com.vmware.labs.stockservice.stock.application.in.ChangePriceUseCase.ChangePriceCommand;
import com.vmware.labs.stockservice.stock.domain.Stock;
import com.vmware.labs.stockservice.stock.domain.events.PriceChanged;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith( SpringExtension.class )
@ContextConfiguration( classes = { StockUpdateEventListener.class } )
class StockUpdateEventListenerTests {

    @MockBean
    ChangePriceUseCase mockChangePriceUseCase;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    private String fakeSymbol = "test";
    private BigDecimal fakePrice = new BigDecimal( "1.00" );
    private Instant fakeOccurredOn = Instant.now();

    @Test
    void testReceiveStockUpdateApplicationEvent() {

        when( this.mockChangePriceUseCase.execute( new ChangePriceCommand( fakeSymbol, fakePrice ) ) ).thenReturn( Mono.just( Stock.createFrom( fakeSymbol, List.of( new PriceChanged( fakeSymbol, fakePrice, fakeOccurredOn ) ) ) ) );

        StockUpdateEvent fakeStockUpdateEvent = new StockUpdateEvent( fakeSymbol, fakePrice );
        this.applicationEventPublisher.publishEvent( fakeStockUpdateEvent );

        ChangePriceCommand expectedCommand = new ChangePriceCommand( fakeSymbol, fakePrice );
        verify( this.mockChangePriceUseCase ).execute( expectedCommand );
        verifyNoMoreInteractions( this.mockChangePriceUseCase );

    }

}
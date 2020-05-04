package com.vmware.labs.stockService.stock.adapter.in.integration;

import com.vmware.labs.stockService.applicationEvents.StockUpdateEvent;
import com.vmware.labs.stockService.stock.application.in.ChangePriceUseCase;
import com.vmware.labs.stockService.stock.application.in.ChangePriceUseCase.ChangePriceCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith( SpringExtension.class )
@ContextConfiguration( classes = { StockUpdateEventListener.class } )
class StockUpdateEventListenerTests {

    @MockBean
    ChangePriceUseCase mockChangePriceUseCase;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    private String fakeSymbol = "test";
    private BigDecimal fakePrice = new BigDecimal( "1.00" );

    @Test
    public void testReceiveStockUpdateApplicationEvent() {

        StockUpdateEvent fakeStockUpdateEvent = new StockUpdateEvent( this, fakeSymbol, fakePrice );
        this.applicationEventPublisher.publishEvent( fakeStockUpdateEvent );

        ChangePriceCommand expectedCommand = new ChangePriceCommand( fakeSymbol, fakePrice );
        verify( this.mockChangePriceUseCase ).execute( expectedCommand );
        verifyNoMoreInteractions( this.mockChangePriceUseCase );

    }

}
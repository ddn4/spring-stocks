package com.vmware.labs.stockService.stock.adapter.in.integration;

import com.vmware.labs.stockService.applicationEvents.StockUpdateEvent;
import com.vmware.labs.stockService.common.integration.IntegrationAdapter;
import com.vmware.labs.stockService.stock.application.in.ChangePriceUseCase;
import com.vmware.labs.stockService.stock.application.in.ChangePriceUseCase.ChangePriceCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;

@Slf4j
@IntegrationAdapter
@RequiredArgsConstructor
public class StockUpdateEventListener implements ApplicationListener<StockUpdateEvent> {

    private final ChangePriceUseCase useCase;

    @Override
    public void onApplicationEvent( StockUpdateEvent event ) {
        log.info( "onApplicationEvent : received event {}", event );

        this.useCase.execute( new ChangePriceCommand( event.getSymbol(), event.getPrice() ) );

    }

}

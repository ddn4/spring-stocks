package com.vmware.labs.stockService.stock.adapter.in.integration;

import com.vmware.labs.stockService.applicationEvents.StockUpdatedEvent;
import com.vmware.labs.stockService.common.integration.IntegrationAdapter;
import com.vmware.labs.stockService.stock.application.in.CacheStockUseCase;
import com.vmware.labs.stockService.stock.application.in.CacheStockUseCase.CacheStockCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;

@Slf4j
@IntegrationAdapter
@RequiredArgsConstructor
public class StockUpdatedEventListener implements ApplicationListener<StockUpdatedEvent> {

    private final CacheStockUseCase useCase;

    @Override
    public void onApplicationEvent( StockUpdatedEvent event ) {
        log.info( "onApplicationEvent : received event {}", event );

        this.useCase.execute( new CacheStockCommand( event.getSymbol() ) );

    }

}

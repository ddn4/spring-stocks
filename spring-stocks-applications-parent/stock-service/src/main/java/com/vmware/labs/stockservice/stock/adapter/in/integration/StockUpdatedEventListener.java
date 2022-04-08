package com.vmware.labs.stockservice.stock.adapter.in.integration;

import com.vmware.labs.common.annotations.integration.IntegrationAdapter;
import com.vmware.labs.stockservice.applicationevents.StockUpdatedEvent;
import com.vmware.labs.stockservice.stock.application.in.CacheStockUseCase;
import com.vmware.labs.stockservice.stock.application.in.CacheStockUseCase.CacheStockCommand;
import com.vmware.labs.stockservice.stock.domain.Stock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import reactor.core.publisher.Mono;

@Slf4j
@IntegrationAdapter
@RequiredArgsConstructor
public class StockUpdatedEventListener {

    private final CacheStockUseCase useCase;

    @EventListener
    public Mono<Stock> handleStockUpdatedEvent( final StockUpdatedEvent event ) {
        log.info( "handleStockUpdatedEvent : received event {}", event );

        return this.useCase.execute( new CacheStockCommand( event.symbol() ) )
                .log();

    }

}

package com.vmware.labs.stockservice.stock.adapter.in.integration;

import com.vmware.labs.stockservice.applicationevents.StockUpdateEvent;
import com.vmware.labs.stockservice.common.integration.IntegrationAdapter;
import com.vmware.labs.stockservice.stock.application.in.ChangePriceUseCase;
import com.vmware.labs.stockservice.stock.application.in.ChangePriceUseCase.ChangePriceCommand;
import com.vmware.labs.stockservice.stock.domain.Stock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import reactor.core.publisher.Mono;

@Slf4j
@IntegrationAdapter
@RequiredArgsConstructor
public class StockUpdateEventListener {

    private final ChangePriceUseCase useCase;

    @EventListener
    public Mono<Stock> handleStockUpdateEvent( final StockUpdateEvent event ) {
        log.info( "handleStockUpdateEvent : received event {}", event );

        return this.useCase.execute( new ChangePriceCommand( event.symbol(), event.price() ) )
                .log();

    }

}

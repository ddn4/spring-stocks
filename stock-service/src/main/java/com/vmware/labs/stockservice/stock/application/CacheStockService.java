package com.vmware.labs.stockservice.stock.application;

import com.vmware.labs.stockservice.common.usecase.UseCase;
import com.vmware.labs.stockservice.stock.application.in.CacheStockUseCase;
import com.vmware.labs.stockservice.stock.application.out.GetStockEventsPort;
import com.vmware.labs.stockservice.stock.application.out.UpdateStockProjectionPort;
import com.vmware.labs.stockservice.stock.domain.Stock;
import com.vmware.labs.stockservice.stock.domain.StockProjection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import static java.util.List.of;

@Slf4j
@UseCase
@RequiredArgsConstructor
public class CacheStockService implements CacheStockUseCase {

    private final GetStockEventsPort getStockEventsPort;
    private final UpdateStockProjectionPort updateStockProjectionPort;

    @Override
    public Mono<Stock> execute( final CacheStockCommand command ) {
        log.debug( "execute : enter" );

        return this.getStockEventsPort.getStockEvents( command.getSymbol() )
                .collectList()
                .map( found -> Stock.createFrom( command.getSymbol(), found ) )
                .switchIfEmpty( Mono.just( Stock.createFrom( command.getSymbol(), of() ) ) )
                .map( stock -> {
                    log.debug( "execute : stock = [{}]", stock );

                    this.updateStockProjectionPort.updateProjection( new StockProjection( stock.symbol(), stock.currentPrice(), stock.lastPriceChange() ) )
                            .subscribe();

                    return stock;
                });
    }

}

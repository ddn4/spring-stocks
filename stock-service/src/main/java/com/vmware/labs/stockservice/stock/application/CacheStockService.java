package com.vmware.labs.stockservice.stock.application;

import com.vmware.labs.stockservice.common.usecase.UseCase;
import com.vmware.labs.stockservice.stock.application.in.CacheStockUseCase;
import com.vmware.labs.stockservice.stock.application.out.GetStockEventsPort;
import com.vmware.labs.stockservice.stock.application.out.PutStockCachePort;
import com.vmware.labs.stockservice.stock.domain.Stock;
import com.vmware.labs.stockservice.stock.domain.StockCache;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@UseCase
@RequiredArgsConstructor
public class CacheStockService implements CacheStockUseCase {

    private final GetStockEventsPort getStockEventsPort;
    private final PutStockCachePort putStockCachePort;

    @Override
    public Mono<Stock> execute( final CacheStockCommand command ) {

        // lookup stock by symbol
        return this.getStockEventsPort.getStockEvents( command.getSymbol() )
                .collectList()
                .map( found -> Stock.createFrom( command.getSymbol(), found ) )
                .doOnNext( Stock::flushChanges )
                .doOnNext( stock -> this.putStockCachePort.cacheStock( new StockCache( stock.symbol(), stock.currentPrice(), stock.lastPriceChange() ) ) );

    }

}

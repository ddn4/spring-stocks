package com.vmware.labs.stockService.stock.application;

import com.vmware.labs.stockService.common.useCase.UseCase;
import com.vmware.labs.stockService.stock.application.in.CacheStockUseCase;
import com.vmware.labs.stockService.stock.application.out.GetStockEventsPort;
import com.vmware.labs.stockService.stock.application.out.PutStockCachePort;
import com.vmware.labs.stockService.stock.domain.Stock;
import com.vmware.labs.stockService.stock.domain.StockCache;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class CacheStockService implements CacheStockUseCase {

    private final GetStockEventsPort getStockEventsPort;
    private final PutStockCachePort putStockCachePort;

    @Override
    public void execute( final CacheStockCommand command ) {

        // lookup stock by symbol
        Stock foundStock = Stock.createFrom( command.getSymbol(), this.getStockEventsPort.getStockEvents( command.getSymbol() ) );
        foundStock.flushChanges();  // We don't want to re-record the already persisted events

        this.putStockCachePort.cacheStock( new StockCache( foundStock.symbol(), foundStock.currentPrice(), foundStock.lastPriceChange() ) );
    }

}

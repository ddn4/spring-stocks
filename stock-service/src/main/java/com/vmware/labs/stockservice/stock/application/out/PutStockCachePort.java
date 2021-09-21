package com.vmware.labs.stockservice.stock.application.out;

import com.vmware.labs.stockservice.stock.domain.StockCache;
import reactor.core.publisher.Mono;

public interface PutStockCachePort {

    Mono<StockCache> cacheStock( StockCache stockCache );

}

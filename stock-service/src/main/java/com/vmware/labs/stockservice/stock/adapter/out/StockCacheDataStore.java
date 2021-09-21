package com.vmware.labs.stockservice.stock.adapter.out;

import reactor.core.publisher.Mono;

interface StockCacheDataStore {

    Mono<StockCacheEntity> findBySymbol( String symbol );

    Mono<StockCacheEntity> save( StockCacheEntity event );

}

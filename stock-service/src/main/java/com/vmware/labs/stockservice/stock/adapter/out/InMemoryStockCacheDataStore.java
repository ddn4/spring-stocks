package com.vmware.labs.stockservice.stock.adapter.out;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
class InMemoryStockCacheDataStore implements StockCacheDataStore {

    private final Map<String, StockCacheEntity> data = new ConcurrentHashMap<>();

    @Override
    public Mono<StockCacheEntity> findBySymbol( final String symbol ) {
        log.debug( "findBySymbol : looking up stock for symbol '{}'", symbol );

        var found = this.data.getOrDefault( symbol, null );
        log.debug( "findBySymbol : found = {}", found );

        return ( null != found ) ? Mono.just( found ) : Mono.empty();
    }

    @Override
    public Mono<StockCacheEntity> save( StockCacheEntity entity ) {

        data.put( entity.symbol(), entity );
        log.debug( "save : saved stock cache for symbol '{} {}'", entity.symbol(), entity );

        log.debug( "data = {}", data );

        return Mono.just( entity );
    }

}

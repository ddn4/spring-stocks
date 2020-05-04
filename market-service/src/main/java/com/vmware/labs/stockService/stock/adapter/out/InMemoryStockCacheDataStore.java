package com.vmware.labs.stockService.stock.adapter.out;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
class InMemoryStockCacheDataStore implements StockCacheDataStore {

    private Map<String, StockCacheEntity> data = new ConcurrentHashMap<>();

    @Override
    public StockCacheEntity findBySymbol( final String symbol ) {
        log.debug( "findBySymbol : looking up stock for symbol '{}'", symbol );

        log.debug( "data = {}", this.data.getOrDefault( symbol, null ) );

        return this.data.getOrDefault( symbol, null );
    }

    @Override
    public void save( StockCacheEntity entity ) {

        data.put( entity.getSymbol(), entity );
        log.debug( "save : saved stock cache for symbol '{} {}'", entity.getSymbol(), entity );

        log.debug( "data = {}", data );

    }

}

package com.vmware.labs.stockService.stock.adapter.out;

interface StockCacheDataStore {

    StockCacheEntity findBySymbol( String symbol );

    void save( StockCacheEntity event );

}

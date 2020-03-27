package com.vmware.labs.stockService.stock.adapter.out;

import java.util.List;

interface StockDataStore {

    List<DomainEventEntity> findBySymbol( String symbol );

    void save( DomainEventEntity event );

}

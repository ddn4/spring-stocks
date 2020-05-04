package com.vmware.labs.stockService.stock.adapter.out;

import java.util.List;

interface StockEventsDataStore {

    List<DomainEventEntity> findBySymbol( String symbol );

    void save( DomainEventEntity event );

}

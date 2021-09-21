package com.vmware.labs.stockservice.stock.adapter.out;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

interface StockEventsDataStore {

    Flux<DomainEventEntity> findBySymbol(String symbol );

    Mono<DomainEventEntity> save(DomainEventEntity event );

}

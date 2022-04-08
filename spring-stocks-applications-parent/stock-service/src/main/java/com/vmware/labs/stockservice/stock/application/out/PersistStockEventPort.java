package com.vmware.labs.stockservice.stock.application.out;

import com.vmware.labs.stockservice.stock.domain.events.DomainEvent;
import reactor.core.publisher.Mono;

public interface PersistStockEventPort {

    Mono<DomainEvent> save( DomainEvent event );

}

package com.vmware.labs.stockservice.stock.application.out;

import com.vmware.labs.stockservice.stock.domain.events.DomainEvent;
import reactor.core.publisher.Flux;

public interface GetStockEventsPort {

    Flux<DomainEvent> getStockEvents( String symbol );

}

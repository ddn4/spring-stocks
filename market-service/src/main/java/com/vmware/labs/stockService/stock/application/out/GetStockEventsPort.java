package com.vmware.labs.stockService.stock.application.out;

import com.vmware.labs.stockService.stock.domain.events.DomainEvent;

import java.util.List;

public interface GetStockEventsPort {

    List<DomainEvent> getStockEvents(String symbol );

}

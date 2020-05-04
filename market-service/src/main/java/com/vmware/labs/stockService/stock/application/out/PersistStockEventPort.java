package com.vmware.labs.stockService.stock.application.out;

import com.vmware.labs.stockService.stock.domain.events.DomainEvent;

public interface PersistStockEventPort {

    void save( DomainEvent event );

}

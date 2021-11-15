package com.vmware.labs.stockservice.applicationevents;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode( callSuper = false )
public class StockUpdatedEvent {

    String symbol;

    public StockUpdatedEvent( String symbol ) {

        this.symbol = symbol;

    }

}

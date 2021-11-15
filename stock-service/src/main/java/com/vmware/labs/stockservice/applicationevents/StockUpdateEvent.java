package com.vmware.labs.stockservice.applicationevents;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.math.BigDecimal;

@Value
@EqualsAndHashCode( callSuper = false )
public class StockUpdateEvent {

    String symbol;
    BigDecimal price;

    public StockUpdateEvent( String symbol, BigDecimal price ) {

        this.symbol = symbol;
        this.price = price;

    }

}

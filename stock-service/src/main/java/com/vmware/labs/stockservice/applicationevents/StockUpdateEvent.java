package com.vmware.labs.stockservice.applicationevents;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.context.ApplicationEvent;

import java.math.BigDecimal;

@Value
@EqualsAndHashCode( callSuper = false )
public class StockUpdateEvent extends ApplicationEvent {

    String symbol;
    BigDecimal price;

    public StockUpdateEvent( Object source, String symbol, BigDecimal price ) {
        super( source );

        this.symbol = symbol;
        this.price = price;

    }

}

package com.vmware.labs.stockService.applicationEvents;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.context.ApplicationEvent;

@Value
@EqualsAndHashCode( callSuper = false )
public class StockUpdatedEvent extends ApplicationEvent {

    String symbol;

    public StockUpdatedEvent( Object source, String symbol ) {
        super( source );

        this.symbol = symbol;

    }

}

package com.vmware.labs.marketservice.applicationevents;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@EqualsAndHashCode( callSuper = false )
public class MarketClosedEvent {

    LocalDateTime timeClosed;

    public MarketClosedEvent( LocalDateTime timeClosed ) {

        this.timeClosed = timeClosed;

    }

}

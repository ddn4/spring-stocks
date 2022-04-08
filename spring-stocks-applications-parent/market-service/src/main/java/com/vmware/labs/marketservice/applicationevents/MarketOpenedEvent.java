package com.vmware.labs.marketservice.applicationevents;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@EqualsAndHashCode( callSuper = false )
public class MarketOpenedEvent {

    LocalDateTime timeOpened;

    public MarketOpenedEvent( LocalDateTime timeOpened ) {

        this.timeOpened = timeOpened;

    }

}

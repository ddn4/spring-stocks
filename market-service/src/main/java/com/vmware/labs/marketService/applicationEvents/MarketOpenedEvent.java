package com.vmware.labs.marketService.applicationEvents;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Value
@EqualsAndHashCode( callSuper = false )
public class MarketOpenedEvent extends ApplicationEvent {

    LocalDateTime timeOpened;

    public MarketOpenedEvent( Object source, LocalDateTime timeOpened ) {
        super( source );

        this.timeOpened = timeOpened;

    }

}

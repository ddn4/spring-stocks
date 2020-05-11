package com.vmware.labs.marketService.applicationEvents;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Value
@EqualsAndHashCode( callSuper = false )
public class MarketClosedEvent extends ApplicationEvent {

    LocalDateTime timeClosed;

    public MarketClosedEvent( Object source, LocalDateTime timeClosed ) {
        super( source );

        this.timeClosed = timeClosed;

    }

}

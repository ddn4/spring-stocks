package com.vmware.labs.marketService.market.adapter.out.integration;

import com.vmware.labs.marketService.applicationEvents.MarketClosedEvent;
import com.vmware.labs.marketService.applicationEvents.MarketOpenedEvent;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class MarketStatusPublisher {

    private final StreamBridge streamBridge;

    @EventListener
    void handleMarketOpenedEvent( final MarketOpenedEvent event ) {
        log.info( "handleMarketOpenedEvent : enter" );

        this.publishStatus( new MarketStatusDomainEvent( "OPENED", event.getTimeOpened() ) );

        log.info( "handleMarketOpenedEvent : exit" );
    }

    @EventListener
    void handleMarketClosedEvent( final MarketClosedEvent event ) {
        log.info( "handleMarketClosedEvent : enter" );

        this.publishStatus( new MarketStatusDomainEvent( "CLOSED", event.getTimeClosed() ) );

        log.info( "handleMarketClosedEvent : exit" );
    }

    private void publishStatus( final MarketStatusDomainEvent event ) {

        this.streamBridge.send( "marketStatus-out-0", event );

    }

    @Value
    class MarketStatusDomainEvent {

        String status;
        LocalDateTime occurred;

    }
}

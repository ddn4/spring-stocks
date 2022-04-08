package com.vmware.labs.marketservice.market.adapter.out.integration;

import com.vmware.labs.marketservice.applicationevents.MarketClosedEvent;
import com.vmware.labs.marketservice.applicationevents.MarketOpenedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MarketStatusPublisher {

    private final StreamBridge output;

    @EventListener
    public void handleMarketOpenedEvent( final MarketOpenedEvent event ) {
        log.info( "handleMarketOpenedEvent : enter" );

        publishStatus(
                MessageBuilder
                        .withPayload( new MarketStatusDomainEvent( "OPEN", event.getTimeOpened() ) )
                        .build()
        );

        log.info( "handleMarketOpenedEvent : exit" );
    }

    @EventListener
    public void handleMarketClosedEvent( final MarketClosedEvent event ) {
        log.info( "handleMarketClosedEvent : enter" );

        publishStatus(
                MessageBuilder
                        .withPayload( new MarketStatusDomainEvent( "CLOSED", event.getTimeClosed() ) )
                        .build()
        );

        log.info( "handleMarketClosedEvent : exit" );
    }

    void publishStatus( final Message<MarketStatusDomainEvent> event ) {

        this.output.send( "marketStatus-out-0", event );

    }

}

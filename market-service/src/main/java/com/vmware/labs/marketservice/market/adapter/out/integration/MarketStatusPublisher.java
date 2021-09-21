package com.vmware.labs.marketservice.market.adapter.out.integration;

import com.vmware.labs.marketservice.applicationevents.MarketClosedEvent;
import com.vmware.labs.marketservice.applicationevents.MarketOpenedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.integration.channel.FluxMessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MarketStatusPublisher {

    private final FluxMessageChannel output;

    @EventListener
    public void handleMarketOpenedEvent( final MarketOpenedEvent event ) {
        log.info( "handleMarketOpenedEvent : enter" );

        this.publishStatus( new MarketStatusDomainEvent( "OPENED", event.getTimeOpened() ) );

        log.info( "handleMarketOpenedEvent : exit" );
    }

    @EventListener
    public void handleMarketClosedEvent( final MarketClosedEvent event ) {
        log.info( "handleMarketClosedEvent : enter" );

        this.publishStatus( new MarketStatusDomainEvent( "CLOSED", event.getTimeClosed() ) );

        log.info( "handleMarketClosedEvent : exit" );
    }

    private void publishStatus( final MarketStatusDomainEvent event ) {

        this.output.send( MessageBuilder.withPayload( event ).build() );

    }

}

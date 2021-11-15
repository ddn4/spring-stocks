package com.vmware.labs.stockservice.market;

import com.vmware.labs.stockservice.applicationevents.MarketStatusUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.integration.channel.FluxMessageChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Slf4j
@Controller
@RequiredArgsConstructor
class MarketStatusEventsGraphqlController {

    private final FluxMessageChannel marketStatusEventsQueue;

    @SubscriptionMapping
    Flux<MarketStatusEventGraphqlView> marketStatusEvents() {

        return Flux.from( marketStatusEventsQueue )
                .map( Message::getPayload )
                .cast( MarketStatusUpdatedEvent.class )
                .map( event -> new MarketStatusEventGraphqlView( MarketStatusType.valueOf( event.marketStatus() ), event.occurred().toString() ) );

    }

    @EventListener
    public void handleMarketStatusUpdatedEvent( final MarketStatusUpdatedEvent event ) {
        log.info( "handleStockUpdatedEvent : received event {}", event );

        marketStatusEventsQueue.send( MessageBuilder.withPayload( event ).build() );

    }

}

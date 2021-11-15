package com.vmware.labs.stockservice.stock.adapter.in.graphql;

import com.vmware.labs.stockservice.applicationevents.StockUpdatedEvent;
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
class StockEventsGraphqlController {

    private final FluxMessageChannel stockEventsQueue;

    @SubscriptionMapping
    Flux<StockEventGraphqlView> stockEvents() {

        return Flux.from( stockEventsQueue )
                .map( Message::getPayload )
                .cast( StockUpdatedEvent.class )
                .map( event -> new StockEventGraphqlView(
                        """
                        Stock [%s] updated
                        """
                        .formatted( event.getSymbol() ) )
                );

    }

    @EventListener
    public void handleStockUpdatedEvent( final StockUpdatedEvent event ) {
        log.info( "handleStockUpdatedEvent : received event {}", event );

        stockEventsQueue.send( MessageBuilder.withPayload( event ).build() );

    }

}

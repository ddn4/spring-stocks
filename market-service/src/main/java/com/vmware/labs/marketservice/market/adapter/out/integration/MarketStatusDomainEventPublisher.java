package com.vmware.labs.marketservice.market.adapter.out.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class MarketStatusDomainEventPublisher {

    private final StreamBridge output;

    void publishStatus( final Message<MarketStatusDomainEvent> event ) {

        this.output.send( "marketStatus-out-0", event );

    }

}


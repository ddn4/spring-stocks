package com.vmware.labs.stockservice.inbound;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vmware.labs.stockservice.applicationevents.MarketStatusUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.time.LocalDateTime;
import java.util.function.Consumer;

@Slf4j
@Configuration
@RequiredArgsConstructor
class MarketStatusListenerConfig {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Bean
    public Consumer<Message<MarketStatus>> marketStatusListener() {

        return event -> {
            log.info( "marketStatusListener : received market status update {}", event );

            this.applicationEventPublisher.publishEvent( new MarketStatusUpdatedEvent( event.getPayload().status(), event.getPayload().occurred() ) );
        };
    }

    @JsonIgnoreProperties( ignoreUnknown = true )
    static record MarketStatus(
            @JsonProperty( "status" ) String status,
            @JsonProperty( "occurred" ) LocalDateTime occurred
    ) {

    }

}

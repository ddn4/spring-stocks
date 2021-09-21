package com.vmware.labs.stockservice.inbound;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vmware.labs.stockservice.applicationevents.StockUpdateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
class StockUpdateListenerConfig {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Bean
    public Consumer<StockUpdate> stockUpdateListener() {

        return event -> this.applicationEventPublisher.publishEvent( new StockUpdateEvent( this, event.symbol(), event.price() ) );
    }

    @JsonIgnoreProperties( ignoreUnknown = true )
    static record StockUpdate(
            @JsonProperty( "symbol" ) String symbol,
            @JsonProperty( "price" ) BigDecimal price ) {

    }

}

package com.vmware.labs.stockService.inbound;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vmware.labs.stockService.applicationEvents.StockUpdateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class StockUpdateListenerConfig {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Bean
    public Consumer<StockUpdate> stockUpdateListener() {

        return event -> this.applicationEventPublisher.publishEvent( new StockUpdateEvent( this, event.getSymbol(), event.getPrice() ) );
    }

    @JsonIgnoreProperties( ignoreUnknown = true )
    static class StockUpdate {

        private final String symbol;
        private final BigDecimal price;

        @JsonCreator
        StockUpdate(
                @JsonProperty( "symbol" ) final String symbol,
                @JsonProperty( "price" ) final BigDecimal price
        ) {

            this.symbol = symbol;
            this.price = price;

        }

        String getSymbol() {

            return this.symbol;
        }

        BigDecimal getPrice() {

            return this.price;
        }

    }

}

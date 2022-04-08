package com.vmware.labs.stockservice.stock.domain.events;

import com.fasterxml.jackson.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;

@JsonIgnoreProperties( ignoreUnknown = true )
@JsonPropertyOrder({ "type", "symbol", "price", "occurredOn" })
public record PriceChanged(
        @JsonProperty( "symbol" ) String symbol,
        @JsonProperty( "price" ) BigDecimal price,
        @JsonProperty( "occurredOn" ) Instant occurredOn
) implements DomainEvent {

    @Override
    public String type() {

        return this.getClass().getSimpleName();
    }

}
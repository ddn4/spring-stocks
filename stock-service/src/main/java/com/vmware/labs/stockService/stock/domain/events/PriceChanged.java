package com.vmware.labs.stockService.stock.domain.events;

import java.math.BigDecimal;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties( ignoreUnknown = true )
@JsonPropertyOrder({ "type", "symbol", "price", "occurredOn" })
public class PriceChanged implements DomainEvent {

    public static final String TYPE = "stock.priceChanged";

    private final String symbol;
    private final BigDecimal price;
    private final Instant occurredOn;

    @JsonCreator
    public PriceChanged(
        @JsonProperty( "symbol" ) String symbol,
        @JsonProperty( "price" ) final BigDecimal price,
        @JsonProperty( "occurredOn" ) final Instant occurredOn
    ) {

        this.symbol = symbol;
        this.price = price;
        this.occurredOn = occurredOn;

    }

    public BigDecimal price() {
        
        return this.price;
    }

    @Override
    public String symbol() {
        
        return this.symbol;
    }

    @Override
    public Instant when() {
        
        return this.occurredOn;
    }

    @Override
    public String type() {

        return TYPE;
    }

}
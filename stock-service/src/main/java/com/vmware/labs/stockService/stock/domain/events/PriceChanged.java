package com.vmware.labs.stockService.stock.domain.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;

import static lombok.AccessLevel.NONE;

@JsonIgnoreProperties( ignoreUnknown = true )
@JsonPropertyOrder({ "type", "symbol", "price", "occurredOn" })
@ToString
@EqualsAndHashCode( callSuper = false )
public class PriceChanged implements DomainEvent {

    @Getter( NONE )
    private final String symbol;

    @Getter( NONE )
    private final BigDecimal price;

    @Getter( NONE )
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

    @JsonProperty( "price" )
    public BigDecimal price() {
        
        return this.price;
    }

    @Override
    @JsonProperty( "symbol" )
    public String symbol() {
        
        return this.symbol;
    }

    @Override
    @JsonProperty( "occurredOn" )
    public Instant when() {
        
        return this.occurredOn;
    }

    @Override
    public String type() {

        return this.getClass().getSimpleName();
    }

}
package com.vmware.labs.stockService.stock.adapter.in.endpoint;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.math.BigDecimal;
import java.time.Instant;

@Value
class StockView {

    String symbol;
    BigDecimal currentPrice;
    Instant priceLastUpdated;

    @JsonCreator
    public StockView(
            @JsonProperty( "symbol" ) final String symbol,
            @JsonProperty( "currentPrice" ) final BigDecimal currentPrice,
            @JsonProperty( "priceLastUpdated" ) final Instant priceLastUpdated
    ) {

        this.symbol = symbol;
        this.currentPrice = currentPrice;
        this.priceLastUpdated = priceLastUpdated;

    }

}

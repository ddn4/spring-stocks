package com.vmware.labs.stockservice.stock.adapter.in.endpoint;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.Instant;

record StockView(
        @JsonProperty( "symbol" ) String symbol,
        @JsonProperty( "currentPrice" ) BigDecimal currentPrice,
        @JsonProperty( "priceLastUpdated" ) Instant priceLastUpdated
) {

}

package com.vmware.labs.stockService.stock.domain;

import lombok.Value;

import java.math.BigDecimal;
import java.time.Instant;

@Value
public
class StockCache {

    String symbol;
    BigDecimal price;
    Instant lastPriceChanged;

}

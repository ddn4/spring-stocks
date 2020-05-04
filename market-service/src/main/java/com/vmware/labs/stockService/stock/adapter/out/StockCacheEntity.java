package com.vmware.labs.stockService.stock.adapter.out;

import lombok.Value;

import java.math.BigDecimal;
import java.time.Instant;

@Value
class StockCacheEntity {

    String symbol;
    BigDecimal price;
    Instant lastPriceChanged;

}

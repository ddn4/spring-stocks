package com.vmware.labs.stockservice.stock.adapter.out;

import java.math.BigDecimal;
import java.time.Instant;

record StockCacheEntity(
        String symbol,
        BigDecimal price,
        Instant lastPriceChanged
) {

}

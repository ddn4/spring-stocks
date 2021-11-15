package com.vmware.labs.stockservice.stock.domain;

import java.math.BigDecimal;
import java.time.Instant;

public record StockProjection(
        String symbol,
        BigDecimal price,
        Instant lastPriceChanged
) {

}

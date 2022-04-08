package com.vmware.labs.stockservice.stock.domain.commands;

import java.math.BigDecimal;
import java.time.Instant;

public final record ChangePrice(
        String symbol,
        BigDecimal price,
        Instant when
) implements Command {

}
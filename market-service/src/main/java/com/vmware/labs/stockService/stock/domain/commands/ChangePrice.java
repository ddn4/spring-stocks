package com.vmware.labs.stockService.stock.domain.commands;

import java.math.BigDecimal;
import java.time.Instant;

import lombok.Value;

@Value
public class ChangePrice implements Command {

    private final String symbol;
    private final BigDecimal price;
    private final Instant when;
    
}
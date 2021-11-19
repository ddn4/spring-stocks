package com.vmware.labs.stockservice.applicationevents;

import java.math.BigDecimal;

public record StockUpdateEvent( String symbol, BigDecimal price ) { }

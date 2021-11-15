package com.vmware.labs.stockservice.stock.adapter.in.graphql;

import java.math.BigDecimal;

record StockGraphqlView( String symbol, BigDecimal currentPrice ) { }
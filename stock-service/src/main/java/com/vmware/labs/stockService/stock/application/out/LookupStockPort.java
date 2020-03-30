package com.vmware.labs.stockService.stock.application.out;

import com.vmware.labs.stockService.stock.domain.StockCache;

public interface LookupStockPort {

    StockCache lookupBySymbol( String symbol );

}

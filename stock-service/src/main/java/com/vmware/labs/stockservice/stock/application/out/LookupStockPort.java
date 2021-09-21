package com.vmware.labs.stockservice.stock.application.out;

import com.vmware.labs.stockservice.stock.domain.StockCache;
import reactor.core.publisher.Mono;

public interface LookupStockPort {

    Mono<StockCache> lookupBySymbol( String symbol );

}

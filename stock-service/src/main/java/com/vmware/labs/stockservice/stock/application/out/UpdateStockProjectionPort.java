package com.vmware.labs.stockservice.stock.application.out;

import com.vmware.labs.stockservice.stock.domain.StockProjection;
import reactor.core.publisher.Mono;

public interface UpdateStockProjectionPort {

    Mono<StockProjection> updateProjection( StockProjection stockProjection );

}

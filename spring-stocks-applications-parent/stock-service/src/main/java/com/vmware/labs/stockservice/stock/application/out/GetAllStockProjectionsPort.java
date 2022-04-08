package com.vmware.labs.stockservice.stock.application.out;

import com.vmware.labs.stockservice.stock.domain.StockProjection;
import reactor.core.publisher.Flux;

public interface GetAllStockProjectionsPort {

    Flux<StockProjection> findAll();

}

package com.vmware.labs.stockservice.stock.application;

import com.vmware.labs.common.annotations.usecase.UseCase;
import com.vmware.labs.stockservice.stock.application.in.RetrieveAllStocksUseCase;
import com.vmware.labs.stockservice.stock.application.out.GetAllStockProjectionsPort;
import com.vmware.labs.stockservice.stock.domain.StockProjection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@UseCase
@RequiredArgsConstructor
public class RetrieveAllStocksService implements RetrieveAllStocksUseCase {

    private final GetAllStockProjectionsPort getAllStockProjectionsPort;

    @Override
    public Flux<StockProjection> execute( final RetrieveAllStocksCommand command ) {
        log.debug( "execute : enter" );

        return Flux.just( command )
                .flatMap( c -> this.getAllStockProjectionsPort.findAll() )
                .log();

    }

}

package com.vmware.labs.stockservice.stock.application;

import com.vmware.labs.common.annotations.usecase.UseCase;
import com.vmware.labs.stockservice.stock.application.in.RetrieveStockUseCase;
import com.vmware.labs.stockservice.stock.application.out.LookupStockProjectionPort;
import com.vmware.labs.stockservice.stock.domain.StockProjection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@UseCase
@RequiredArgsConstructor
public class RetrieveStockService implements RetrieveStockUseCase {

    private final LookupStockProjectionPort lookupStockProjectionPort;

    @Override
    public Mono<StockProjection> execute(final RetrieveStockCommand command ) {
        log.debug( "execute : enter" );

        return Mono.just( command )
                .flatMap( c -> this.lookupStockProjectionPort.lookupBySymbol( c.getSymbol() ) )
                .log()
                .switchIfEmpty( Mono.error( new StockNotFoundException( command.getSymbol() ) ) );

    }

}

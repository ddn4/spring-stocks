package com.vmware.labs.stockservice.stock.application;

import com.vmware.labs.stockservice.common.usecase.UseCase;
import com.vmware.labs.stockservice.stock.application.in.RetrieveStockUseCase;
import com.vmware.labs.stockservice.stock.application.out.LookupStockPort;
import com.vmware.labs.stockservice.stock.domain.StockCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@UseCase
@RequiredArgsConstructor
public class RetrieveStockService implements RetrieveStockUseCase {

    private final LookupStockPort lookupStockPort;

    @Override
    public Mono<StockCache> execute( final RetrieveStockCommand command ) {
        log.debug( "execute : enter" );

        return Mono.just( command )
                .flatMap( c -> this.lookupStockPort.lookupBySymbol( c.getSymbol() ) )
                .log()
                .switchIfEmpty( Mono.error( new StockNotFoundException( command.getSymbol() ) ) );

    }

}

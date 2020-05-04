package com.vmware.labs.stockService.stock.application;

import com.vmware.labs.stockService.common.useCase.UseCase;
import com.vmware.labs.stockService.stock.application.in.RetrieveStockUseCase;
import com.vmware.labs.stockService.stock.application.out.LookupStockPort;
import com.vmware.labs.stockService.stock.application.out.StockExistsPort;
import com.vmware.labs.stockService.stock.domain.StockCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@UseCase
@RequiredArgsConstructor
public class RetrieveStockService implements RetrieveStockUseCase {

    private final StockExistsPort stockExistsPort;
    private final LookupStockPort lookupStockPort;

    @Override
    public Mono<StockCache> execute( final RetrieveStockCommand command ) {
        log.debug( "execute : enter" );

        if( !this.stockExistsPort.exists( command.getSymbol() ) ) {
            log.warn( "execute : stock [{}] not found", command.getSymbol() );

            return Mono.error( new StockNotFoundException( command.getSymbol() ) );
        }

        StockCache foundStock = this.lookupStockPort.lookupBySymbol( command.getSymbol() );
        log.debug( "execute : found stock {}", foundStock );

        log.debug( "execute : exit" );
        return Mono.just( foundStock );
    }

}

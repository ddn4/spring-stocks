package com.vmware.labs.stockService.stock.application.in;

import com.vmware.labs.stockService.common.useCase.SelfValidating;
import com.vmware.labs.stockService.stock.domain.StockCache;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotEmpty;

public interface RetrieveStockUseCase {

    Mono<StockCache> execute( RetrieveStockCommand command ) throws StockNotFoundException;

    @Getter
    @EqualsAndHashCode( callSuper = false )
    final class RetrieveStockCommand extends SelfValidating<RetrieveStockCommand> {

        @NotEmpty
        private final String symbol;

        public RetrieveStockCommand( @NotEmpty String symbol ) {

            this.symbol = symbol;

            validateSelf();

        }

    }

    final class StockNotFoundException extends RuntimeException {

        private final String symbol;

        public StockNotFoundException( final String symbol ) {
            super( String.format( "Stock [%s] not found!", symbol ) );

            this.symbol = symbol;

        }

        public StockNotFoundException( final String symbol, final Throwable throwable ) {
            super( String.format( "Stock [%s] not found!", symbol ), throwable );

            this.symbol = symbol;

        }

    }

}

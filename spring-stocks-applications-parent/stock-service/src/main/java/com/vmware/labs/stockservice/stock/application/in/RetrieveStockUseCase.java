package com.vmware.labs.stockservice.stock.application.in;

import com.vmware.labs.common.annotations.usecase.SelfValidating;
import com.vmware.labs.stockservice.stock.domain.StockProjection;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotEmpty;

public interface RetrieveStockUseCase {

    Mono<StockProjection> execute(RetrieveStockCommand command ) throws StockNotFoundException;

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

        public StockNotFoundException( final String symbol ) {
            super( String.format( "Stock [%s] not found!", symbol ) );

        }

    }

}

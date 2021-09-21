package com.vmware.labs.stockservice.stock.application.in;

import com.vmware.labs.stockservice.common.usecase.SelfValidating;
import com.vmware.labs.stockservice.stock.domain.Stock;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotEmpty;

public interface CacheStockUseCase {

    Mono<Stock> execute( CacheStockCommand command );

    @Getter
    @EqualsAndHashCode( callSuper = false )
    final class CacheStockCommand extends SelfValidating<CacheStockCommand> {

        @NotEmpty
        private final String symbol;

        public CacheStockCommand( @NotEmpty String symbol ) {

            this.symbol = symbol;

            validateSelf();

        }

    }

}

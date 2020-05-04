package com.vmware.labs.stockService.stock.application.in;

import com.vmware.labs.stockService.common.useCase.SelfValidating;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;

public interface CacheStockUseCase {

    void execute( CacheStockCommand command );

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

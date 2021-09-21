package com.vmware.labs.stockservice.stock.application.in;

import com.vmware.labs.stockservice.common.usecase.SelfValidating;
import com.vmware.labs.stockservice.stock.domain.Stock;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public interface ChangePriceUseCase {

    Mono<Stock> execute( ChangePriceCommand command );

    @Getter
    @EqualsAndHashCode( callSuper = false )
    final class ChangePriceCommand extends SelfValidating<ChangePriceCommand> {

        @NotEmpty
        private final String symbol;

        @NotNull
        private final BigDecimal price;


        public ChangePriceCommand( @NotEmpty String symbol, @NotNull BigDecimal price ) {

            this.symbol = symbol;
            this.price = price;

            validateSelf();

        }

    }

}

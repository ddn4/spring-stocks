package com.vmware.labs.stockService.stock.application.in;

import com.vmware.labs.stockService.common.useCase.SelfValidating;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public interface ChangePriceUseCase {

    void execute( ChangePriceCommand command );

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

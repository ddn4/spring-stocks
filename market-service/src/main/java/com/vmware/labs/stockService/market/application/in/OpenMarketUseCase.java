package com.vmware.labs.stockService.market.application.in;

import com.vmware.labs.stockService.common.useCase.SelfValidating;

import lombok.EqualsAndHashCode;
import lombok.Getter;

public interface OpenMarketUseCase {

    void execute( OpenMarketCommand command );

    @Getter
    @EqualsAndHashCode( callSuper = false )
    final class OpenMarketCommand extends SelfValidating<OpenMarketCommand> {

        public OpenMarketCommand() {

            validateSelf();

        }

    }

}

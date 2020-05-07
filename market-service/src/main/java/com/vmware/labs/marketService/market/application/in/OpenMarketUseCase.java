package com.vmware.labs.marketService.market.application.in;

import com.vmware.labs.marketService.common.useCase.SelfValidating;

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

package com.vmware.labs.marketService.market.application.in;

import com.vmware.labs.marketService.common.useCase.SelfValidating;

import lombok.EqualsAndHashCode;
import lombok.Getter;

public interface CloseMarketUseCase {

    void execute( CloseMarketCommand command );

    @Getter
    @EqualsAndHashCode( callSuper = false )
    final class CloseMarketCommand extends SelfValidating<CloseMarketCommand> {

        public CloseMarketCommand() {

            validateSelf();

        }

    }

}

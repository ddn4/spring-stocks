package com.vmware.labs.marketService.market.application.in;

import com.vmware.labs.marketService.common.useCase.SelfValidating;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

public interface CloseMarketUseCase {

    void execute( CloseMarketCommand command );

    @Getter
    @EqualsAndHashCode( callSuper = false )
    @ToString
    final class CloseMarketCommand extends SelfValidating<CloseMarketCommand> {

        final LocalDateTime timeClosed;

        public CloseMarketCommand() {

            this.timeClosed = LocalDateTime.now();

            validateSelf();

        }

    }

}

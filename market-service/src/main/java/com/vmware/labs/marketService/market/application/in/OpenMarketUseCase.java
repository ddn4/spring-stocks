package com.vmware.labs.marketService.market.application.in;

import com.vmware.labs.marketService.common.useCase.SelfValidating;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

public interface OpenMarketUseCase {

    void execute( OpenMarketCommand command );

    @Getter
    @EqualsAndHashCode( callSuper = false )
    final class OpenMarketCommand extends SelfValidating<OpenMarketCommand> {

        final LocalDateTime timeOpened;

        public OpenMarketCommand() {

            this.timeOpened = LocalDateTime.now();

            validateSelf();

        }

    }

}

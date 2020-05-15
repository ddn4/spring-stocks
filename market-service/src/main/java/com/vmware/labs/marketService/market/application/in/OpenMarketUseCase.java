package com.vmware.labs.marketService.market.application.in;

import com.vmware.labs.marketService.common.useCase.SelfValidating;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

public interface OpenMarketUseCase {

    void execute( OpenMarketCommand command );

    @Getter
    @EqualsAndHashCode( callSuper = false )
    @ToString
    final class OpenMarketCommand extends SelfValidating<OpenMarketCommand> {

        final LocalDateTime timeOpened;

        public OpenMarketCommand() {

            this.timeOpened = LocalDateTime.now();

            validateSelf();

        }

    }

}

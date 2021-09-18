package com.vmware.labs.marketService.market.application.in;

import com.vmware.labs.marketService.common.useCase.SelfValidating;
import com.vmware.labs.marketService.market.application.MarketStatusState;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public sealed interface OpenMarketUseCase permits OpenMarketService {

    Mono<MarketStatusState> execute( final OpenMarketCommand command );

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

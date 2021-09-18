package com.vmware.labs.marketService.market.application.in;

import com.vmware.labs.marketService.common.useCase.SelfValidating;
import com.vmware.labs.marketService.market.application.MarketStatusState;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public sealed interface CloseMarketUseCase permits CloseMarketService {

    Mono<MarketStatusState> execute( final CloseMarketCommand command );

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

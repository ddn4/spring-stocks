package com.vmware.labs.marketservice.market.application.in;

import com.vmware.labs.marketservice.common.usecase.SelfValidating;
import com.vmware.labs.marketservice.market.application.MarketStatusState;
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
            this( LocalDateTime.now() );
        }

        public OpenMarketCommand( final LocalDateTime timeOpened ) {

            this.timeOpened = timeOpened;

            validateSelf();

        }

    }

}

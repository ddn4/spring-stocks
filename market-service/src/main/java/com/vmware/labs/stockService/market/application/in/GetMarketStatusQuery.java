package com.vmware.labs.stockService.market.application.in;

import com.vmware.labs.stockService.common.useCase.SelfValidating;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface GetMarketStatusQuery {

    Mono<Map<String, Object>> execute( GetMarketStatusCommand command );

    @Getter
    @EqualsAndHashCode( callSuper = false )
    final class GetMarketStatusCommand extends SelfValidating<GetMarketStatusCommand> {

        public GetMarketStatusCommand() {

            validateSelf();

        }

    }

}

package com.vmware.labs.marketservice.market.application.in;

import com.vmware.labs.marketservice.common.usecase.SelfValidating;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import reactor.core.publisher.Mono;

import java.util.Map;

public sealed interface GetMarketStatusQuery permits GetMarketStatusService {

    Mono<Map<String, Object>> execute( GetMarketStatusCommand command );

    @Getter
    @EqualsAndHashCode( callSuper = false )
    final class GetMarketStatusCommand extends SelfValidating<GetMarketStatusCommand> {

        public GetMarketStatusCommand() {

            validateSelf();

        }

    }

}

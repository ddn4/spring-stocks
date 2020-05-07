package com.vmware.labs.marketService.market.application;

import com.vmware.labs.marketService.common.useCase.UseCase;
import com.vmware.labs.marketService.market.application.in.GetMarketStatusQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@UseCase
@RequiredArgsConstructor
public class GetMarketStatusService implements GetMarketStatusQuery {

    @Override
    public Mono<Map<String, Object>> execute( GetMarketStatusCommand command ) {

        return
                Mono.just(
                        Map.of( "marketStatus", MarketStatus.OPEN )
                );
    }

}

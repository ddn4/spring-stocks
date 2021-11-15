package com.vmware.labs.marketservice.market.application.in;

import com.vmware.labs.marketservice.common.usecase.UseCase;
import com.vmware.labs.marketservice.market.application.out.GetMarketStatusPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@UseCase
@RequiredArgsConstructor
public final class GetMarketStatusService implements GetMarketStatusQuery {

    private final GetMarketStatusPort getMarketStatusPort;

    @Override
    public Mono<Map<String, Object>> execute( final GetMarketStatusCommand command ) {

        return this.getMarketStatusPort.currentStatus()
                .log()
                .map( found -> Map.of(
                        "marketStatus", found.status().name(),
                        "occurred", found.occurred().toString()
                ));

    }

}

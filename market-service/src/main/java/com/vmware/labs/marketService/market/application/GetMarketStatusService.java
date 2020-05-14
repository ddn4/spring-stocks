package com.vmware.labs.marketService.market.application;

import com.vmware.labs.marketService.common.useCase.UseCase;
import com.vmware.labs.marketService.market.application.in.GetMarketStatusQuery;
import com.vmware.labs.marketService.market.application.out.GetMarketStatusPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@UseCase
@RequiredArgsConstructor
public class GetMarketStatusService implements GetMarketStatusQuery {

    private final GetMarketStatusPort getMarketStatusPort;

    @Override
    public Mono<Map<String, Object>> execute( GetMarketStatusCommand command ) {

        CurrentMarketStatus found = this.getMarketStatusPort.currentStatus();

        return

                Mono.just(
                        Map.of(
                                "marketStatus", found.getStatus().name(),
                                "occurred", found.getOccurred().toString()
                        )
                );
    }

}

package com.vmware.labs.stockservice.market.adapter.in.webclient;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

interface MarketStatusHandler {

    Mono<ServerResponse> retrieveMarketStatus( ServerRequest request );

}

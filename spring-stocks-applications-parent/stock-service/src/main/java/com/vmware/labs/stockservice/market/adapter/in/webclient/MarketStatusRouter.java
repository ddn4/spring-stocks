package com.vmware.labs.stockservice.market.adapter.in.webclient;

import com.vmware.labs.common.annotations.endpoint.EndpointAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@EndpointAdapter
@RequiredArgsConstructor
class MarketStatusRouter {

    private final MarketStatusHandler handler;

    @Bean
    RouterFunction<ServerResponse> marketStatusRoutes() {

        return route()
                .GET( "/market-status", handler::retrieveMarketStatus )
                .build();
    }

}

package com.vmware.labs.stockService.stock.adapter.in.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
class StockRouter {

    private final StockHandler handler;

    @Bean
    RouterFunction<ServerResponse> stockRoutes() {

        return route()
                .path( "/api/stocks", builder -> builder
                    .PUT( "/{symbol}", handler::updateStock )
                )
                .build();
    }

}

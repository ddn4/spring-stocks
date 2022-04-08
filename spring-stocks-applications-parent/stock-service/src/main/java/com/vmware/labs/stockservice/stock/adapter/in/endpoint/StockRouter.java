package com.vmware.labs.stockservice.stock.adapter.in.endpoint;

import com.vmware.labs.common.annotations.endpoint.EndpointAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@EndpointAdapter
@RequiredArgsConstructor
class StockRouter {

    private final StockHandler handler;

    @Bean
    RouterFunction<ServerResponse> stockRoutes() {

        return route()
                .nest( path( "/api/stocks" ), builder -> builder
                    .GET( "/{symbol}", handler::retrieveStock )
                    .PUT( "/{symbol}", handler::updateStock )
                )
                .build();
    }

}

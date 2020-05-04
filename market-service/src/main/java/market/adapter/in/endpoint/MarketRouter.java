package market.adapter.in.endpoint;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
class MarketRouter {

    private final MarketHandler handler;

    @Bean
    RouterFunction<ServerResponse> marketRoutes() {

        return route()
                .nest( path( "/api/market" ), builder -> builder
                    .GET( "", handler::retrieveMarketStatus )
                    .PUT( "/open", handler::openMarket )
                    .PUT( "/close", handler::closeMarket )
                )
                .build();
    }

{
    
}
package com.vmware.labs.marketservice.market.adapter.in.endpoint;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.time.LocalDateTime;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
class MarketRouter {

    private final MarketHandler handler;

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/market", method = RequestMethod.GET,
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    beanClass = MarketHandler.class, beanMethod = "retrieveMarketStatus",
                    operation = @Operation(
                            operationId = "marketStatus",
                            summary = "API endpoint to query current market status",
                            tags = { "query" },
                            responses = {
                                    @ApiResponse(
                                            description = "Market API that queries the current state of the market",
                                            responseCode = "200",
                                            content = {
                                                    @Content(
                                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                            schema =@Schema(
                                                                    implementation = MarketStatusSchema.class
                                                            )
                                                    )
                                            }
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/market/open", method = RequestMethod.PUT,
                    beanClass = MarketHandler.class, beanMethod = "openMarket",
                    operation = @Operation(
                            operationId = "openMarket",
                            summary = "API endpoint to open the market",
                            tags = { "update" },
                            responses = {
                                    @ApiResponse(
                                            description = "Market API that opens the market",
                                            responseCode = "202"
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/market/close", method = RequestMethod.PUT,
                    beanClass = MarketHandler.class, beanMethod = "closeMarket",
                    operation = @Operation(
                            operationId = "closeMarket",
                            summary = "API endpoint to close the market",
                            tags = { "update" },
                            responses = {
                                    @ApiResponse(
                                            description = "Market API that closes the market",
                                            responseCode = "202"
                                    )
                            }
                    )
            )
    })
    RouterFunction<ServerResponse> marketRoutes() {

        return route()
                .nest( path( "/api/market" ), builder -> builder

                        .GET( "", handler::retrieveMarketStatus )

                        .PUT( "/open", handler::openMarket )

                        .PUT( "/close", handler::closeMarket )
                )
                .build();
    }

    record MarketStatusSchema( String marketStatus, LocalDateTime occurred ) {}

}

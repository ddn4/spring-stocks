package com.vmware.labs.stockService.market;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@Profile( "kubernetes" )
@RequiredArgsConstructor
public class MarketStatusHandler {

    private final WebClient.Builder loadBalancedWebClientBuilder;
    private final ReactiveCircuitBreakerFactory circuitBreakerFactory;

    public Mono<ServerResponse> retrieveMarketStatus( ServerRequest request ) {

        return ServerResponse.ok()
                .body(
                        this.loadBalancedWebClientBuilder.build()
                                .get()
                                    .uri( "http://market-service/api/market" )
                                .retrieve()
                                    .bodyToMono( String.class )
                                    .transform( it ->
                                            circuitBreakerFactory
                                                    .create( "market-status" )
                                                    .run( it, throwable -> {
                                                        log.warn( "retrieveMarketStatus : fallback initiated!" );

                                                        return Mono.just( "{\"marketStatus\": \"market status unavailable\"}" );
                                                    })
                                    ),
                        String.class
                );
    }

}

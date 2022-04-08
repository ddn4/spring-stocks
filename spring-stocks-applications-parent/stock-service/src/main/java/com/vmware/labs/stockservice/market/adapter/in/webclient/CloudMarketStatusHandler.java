package com.vmware.labs.stockservice.market.adapter.in.webclient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnCloudPlatform;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.boot.cloud.CloudPlatform.KUBERNETES;

@Slf4j
@Component
@ConditionalOnCloudPlatform( KUBERNETES )
@RequiredArgsConstructor
public class CloudMarketStatusHandler implements MarketStatusHandler {

    private final WebClient.Builder loadBalancedWebClientBuilder;
    private final ReactiveCircuitBreakerFactory circuitBreakerFactory;

    /**
     *
     * @param request <code>ServerRequest</code> is required for server handlers
     * @return <code>Mono<ServerResponse></code>
     */
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

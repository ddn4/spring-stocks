package com.vmware.labs.stockService.market;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@Profile( "kubernetes" )
@RequiredArgsConstructor
public class MarketStatusHandler {

    private final WebClient.Builder loadBalancedWebClientBuilder;

    public Mono<ServerResponse> retrieveMarketStatus( ServerRequest request ) {

        return ServerResponse.ok()
                .body(
                        this.loadBalancedWebClientBuilder
                                .build()
                                .get()
                                    .uri( "http://market-service/api/market" )
                                .retrieve()
                                    .bodyToMono( String.class ),
                        String.class
                );
    }

}

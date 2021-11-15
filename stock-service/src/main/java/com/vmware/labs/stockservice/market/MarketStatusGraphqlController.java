package com.vmware.labs.stockservice.market;

import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
class MarketStatusGraphqlController {

    private final WebClient.Builder webClientBuilder;

    @QueryMapping
    Mono<MarketStatusView> marketStatus() {

        return this.webClientBuilder.build()
                .get()
                    .uri( "http://localhost:7081/api/market" )
                .retrieve()
                    .bodyToMono( MarketStatusView.class );
    }

}

record MarketStatusView( MarketStatusType marketStatus, String occurred ) { }
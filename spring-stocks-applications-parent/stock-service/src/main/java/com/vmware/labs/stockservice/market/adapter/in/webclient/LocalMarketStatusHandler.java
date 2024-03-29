package com.vmware.labs.stockservice.market.adapter.in.webclient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnCloudPlatform;
import org.springframework.boot.autoconfigure.condition.NoneNestedConditions;
import org.springframework.boot.cloud.CloudPlatform;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
//@Profile({ "!cloud", "!kubernetes" })
@Conditional( NotOnCloudPlatformCondition.class )
@RequiredArgsConstructor
public class LocalMarketStatusHandler implements MarketStatusHandler {

    private final WebClient.Builder webClientBuilder;

    /**
     *
     * @param request <code>ServerRequest</code> is required for server handlers
     * @return <code>Mono<ServerResponse></code>
     */
    public Mono<ServerResponse> retrieveMarketStatus( ServerRequest request ) {
        log.debug( "retrieveMarketStatus : enter" );

        return ServerResponse.ok()
                .body(
                        this.webClientBuilder.build()
                                .get()
                                    .uri( "http://localhost:7081/api/market" )
                                .retrieve()
                                    .bodyToMono( String.class )
                                    .log(),
                        String.class
                );
    }

}

class NotOnCloudPlatformCondition extends NoneNestedConditions {

    public NotOnCloudPlatformCondition() {
        super( ConfigurationPhase.PARSE_CONFIGURATION );

    }

    @ConditionalOnCloudPlatform( CloudPlatform.KUBERNETES )
    static class KubernetesCondition { }

    @ConditionalOnCloudPlatform( CloudPlatform.CLOUD_FOUNDRY )
    static class CloudFoundryCondition { }

}

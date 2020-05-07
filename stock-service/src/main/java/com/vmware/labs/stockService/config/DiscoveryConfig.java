package com.vmware.labs.stockService.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
@EnableDiscoveryClient
@EnableScheduling
@Profile( "kubernetes" )
@RestController
@RequiredArgsConstructor
public class DiscoveryConfig {

    private final WebClient.Builder loadBalancedWebClientBuilder;

    @GetMapping( "/market-status" )
    Mono<String> getMarketStatus() {

        return this.loadBalancedWebClientBuilder
                .build()
                .get()
                .uri( "http://market-service/api/market" )
                .retrieve()
                .bodyToMono( String.class );
    }

    @Configuration
    @Profile( "kubernetes" )
    static class WebClientConfig {

        @Bean
        @LoadBalanced
        WebClient.Builder loadBalancedWebClientBuilder() {

            return WebClient.builder();
        }

    }

}

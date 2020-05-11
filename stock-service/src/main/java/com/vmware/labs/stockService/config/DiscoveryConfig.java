package com.vmware.labs.stockService.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableDiscoveryClient
@EnableScheduling
@Profile( "kubernetes" )
@RequiredArgsConstructor
public class DiscoveryConfig {

    private final WebClient.Builder loadBalancedWebClientBuilder;

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

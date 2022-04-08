package com.vmware.labs.stockservice.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnCloudPlatform;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.boot.cloud.CloudPlatform.KUBERNETES;

@Configuration
@EnableDiscoveryClient
@EnableScheduling
@ConditionalOnCloudPlatform( KUBERNETES )
class DiscoveryConfig {

    @Bean
    @LoadBalanced
    WebClient.Builder loadBalancedWebClientBuilder() {

        return WebClient.builder();
    }

}

package com.vmware.labs.stockService.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableDiscoveryClient
@EnableScheduling
@Profile( "kubernetes" )
public class DiscoveryConfig {
}

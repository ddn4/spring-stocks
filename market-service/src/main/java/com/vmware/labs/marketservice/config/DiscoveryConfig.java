package com.vmware.labs.marketservice.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnCloudPlatform;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import static org.springframework.boot.cloud.CloudPlatform.KUBERNETES;

@Configuration
@EnableDiscoveryClient
@EnableScheduling
@ConditionalOnCloudPlatform( KUBERNETES )
public class DiscoveryConfig {
}

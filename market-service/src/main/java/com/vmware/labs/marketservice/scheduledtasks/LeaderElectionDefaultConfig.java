package com.vmware.labs.marketservice.scheduledtasks;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.support.locks.DefaultLockRegistry;
import org.springframework.integration.support.locks.LockRegistry;

@Configuration( proxyBeanMethods = false )
@Profile( { "!cloud", "!kubernetes" })
public class LeaderElectionDefaultConfig {

    @Bean
    LockRegistry lockRegistry() {

        return new DefaultLockRegistry();
    }

}

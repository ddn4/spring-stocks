package com.vmware.labs.marketservice.scheduledtasks;

import org.springframework.boot.autoconfigure.condition.ConditionalOnCloudPlatform;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.jdbc.lock.DefaultLockRepository;
import org.springframework.integration.jdbc.lock.JdbcLockRegistry;
import org.springframework.integration.jdbc.lock.LockRepository;
import org.springframework.integration.support.locks.LockRegistry;

import javax.sql.DataSource;

import static org.springframework.boot.cloud.CloudPlatform.KUBERNETES;

@Configuration( proxyBeanMethods = false )
@ConditionalOnCloudPlatform( KUBERNETES )
@Profile({ "cloud" })
public class LeaderElectionCloudConfig {

    @Bean
    LockRepository lockRepository( final DataSource dataSource ) {

        return new DefaultLockRepository( dataSource );
    }

    @Bean
    LockRegistry lockRegistry( final LockRepository lockRepository ) {

        return new JdbcLockRegistry( lockRepository );
    }

}

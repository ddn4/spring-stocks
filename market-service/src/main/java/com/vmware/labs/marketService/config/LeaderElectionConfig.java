package com.vmware.labs.marketService.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnCloudPlatform;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.integration.jdbc.lock.DefaultLockRepository;
import org.springframework.integration.jdbc.lock.JdbcLockRegistry;
import org.springframework.integration.jdbc.lock.LockRepository;
import org.springframework.integration.leader.event.OnGrantedEvent;
import org.springframework.integration.leader.event.OnRevokedEvent;
import org.springframework.integration.support.leader.LockRegistryLeaderInitiator;
import org.springframework.integration.support.locks.DefaultLockRegistry;
import org.springframework.integration.support.locks.LockRegistry;

import javax.sql.DataSource;

import static org.springframework.boot.cloud.CloudPlatform.KUBERNETES;

@Configuration
@Slf4j
public class DistributedLockConfig {

    @Bean
    public LockRegistryLeaderInitiator leaderInitiator(
            final LockRegistry locks,
            final ApplicationEventPublisher applicationEventPublisher
    ) {

        var leaderInitiator = new LockRegistryLeaderInitiator( locks );
        leaderInitiator.setApplicationEventPublisher( applicationEventPublisher );

        return new LockRegistryLeaderInitiator( locks );
    }

    @EventListener( OnGrantedEvent.class )
    public void leaderGranted( OnGrantedEvent event ) {
        log.info( "I am the leader [{}]", event );

    }

    @EventListener( OnRevokedEvent.class )
    public void leaderRevoked( OnRevokedEvent event ) {
        log.info( "I am no longer the leader [{}]", event );

    }

    @Configuration
    @Profile( { "!cloud", "!kubernetes" })
    static class Default {

        @Bean
        LockRegistry lockRegistry() {

            return new DefaultLockRegistry();
        }

    }

    @Configuration
    @ConditionalOnCloudPlatform( KUBERNETES )
    @Profile({ "cloud" })
    static class Cloud {

        @Bean
        LockRepository lockRepository( final DataSource dataSource ) {

            return new DefaultLockRepository( dataSource );
        }

        @Bean
        LockRegistry lockRegistry( final LockRepository lockRepository ) {

            return new JdbcLockRegistry( lockRepository );
        }

    }

}

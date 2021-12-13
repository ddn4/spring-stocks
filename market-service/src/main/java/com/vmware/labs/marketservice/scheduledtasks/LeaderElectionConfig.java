package com.vmware.labs.marketservice.scheduledtasks;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.integration.leader.Candidate;
import org.springframework.integration.leader.DefaultCandidate;
import org.springframework.integration.leader.event.OnGrantedEvent;
import org.springframework.integration.leader.event.OnRevokedEvent;
import org.springframework.integration.support.leader.LockRegistryLeaderInitiator;
import org.springframework.integration.support.locks.LockRegistry;

import java.util.UUID;

@Configuration( proxyBeanMethods = false )
//@Slf4j
class LeaderElectionConfig {

    @Bean
    Candidate candidate() {

        return new DefaultCandidate( UUID.randomUUID().toString(), "marketStatusLeader" );
    }

    @Bean
    LockRegistryLeaderInitiator leaderInitiator(
            final LockRegistry locks,
            final Candidate candidate,
            final ApplicationEventPublisher applicationEventPublisher
    ) {

        var leaderInitiator = new LockRegistryLeaderInitiator( locks, candidate );
        leaderInitiator.setApplicationEventPublisher( applicationEventPublisher );

        return leaderInitiator;
    }

    @EventListener( OnGrantedEvent.class )
    public void leaderGranted( OnGrantedEvent event ) {
//        log.info( "I am the leader [{}]", event );

    }

    @EventListener( OnRevokedEvent.class )
    public void leaderRevoked( OnRevokedEvent event ) {
//        log.info( "I am no longer the leader [{}]", event );

    }

//    @Configuration
//    @Profile( { "!cloud", "!kubernetes" })
//    static class Default {
//
//        @Bean
//        LockRegistry lockRegistry() {
//
//            return new DefaultLockRegistry();
//        }
//
//    }

//    @Configuration
//    @ConditionalOnCloudPlatform( KUBERNETES )
//    @Profile({ "cloud" })
//    static class Cloud {
//
//        @Bean
//        LockRepository lockRepository( final DataSource dataSource ) {
//
//            return new DefaultLockRepository( dataSource );
//        }
//
//        @Bean
//        LockRegistry lockRegistry( final LockRepository lockRepository ) {
//
//            return new JdbcLockRegistry( lockRepository );
//        }
//
//    }

}

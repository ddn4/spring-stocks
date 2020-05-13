package com.vmware.labs.marketService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.jdbc.lock.DefaultLockRepository;
import org.springframework.integration.jdbc.lock.JdbcLockRegistry;
import org.springframework.integration.jdbc.lock.LockRepository;

import javax.sql.DataSource;

@Configuration
public class DistributedLockConfig {

    @Bean
    DefaultLockRepository defaultLockRepository( DataSource dataSource ) {

        return new DefaultLockRepository( dataSource );
    }

    @Bean
    JdbcLockRegistry jdbcLockRegistry( LockRepository repository ) {

        return new JdbcLockRegistry( repository );
    }

}

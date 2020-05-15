package com.vmware.labs.marketService.config;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DistributedLockConfig {

    @Bean
    LockProvider lockProvider( final DataSource dataSource ) {

        return new JdbcTemplateLockProvider(
                JdbcTemplateLockProvider.Configuration.builder()
                        .withJdbcTemplate( new JdbcTemplate( dataSource ) )
                        .usingDbTime()
                        .build()
        );
    }

}

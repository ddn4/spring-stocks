package com.vmware.labs.components.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration( proxyBeanMethods = false )
public class TimestampGeneratorConfigurations {


    @Bean
    @ConditionalOnMissingBean( TimestampGenerator.class )
    TimestampGenerator timestampGenerator() {
        
        Clock clock = Clock.systemUTC();
        
        return new TimestampGenerator( clock );
    }
    
}

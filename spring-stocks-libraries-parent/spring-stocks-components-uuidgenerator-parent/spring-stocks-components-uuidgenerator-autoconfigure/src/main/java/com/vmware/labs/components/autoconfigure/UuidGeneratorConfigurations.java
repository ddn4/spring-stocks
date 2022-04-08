package com.vmware.labs.components.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration( proxyBeanMethods = false )
public class UuidGeneratorConfigurations {


    @Bean
    @ConditionalOnMissingBean( UuidGenerator.class )
    UuidGenerator uuidGenerator() {

        return new UuidGenerator();
    }
    
}

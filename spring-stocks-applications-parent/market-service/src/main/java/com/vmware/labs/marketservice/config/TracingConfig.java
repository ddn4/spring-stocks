package com.vmware.labs.marketservice.config;

import brave.sampler.Sampler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static brave.sampler.Sampler.ALWAYS_SAMPLE;

@Configuration
public class TracingConfig {

    @Bean
    public Sampler defaultSampler() {

        return ALWAYS_SAMPLE;
    }

}

package com.vmware.labs.stockservice.stock.adapter.in.graphql;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.FluxMessageChannel;

@Slf4j
@Configuration
@RequiredArgsConstructor
class StockUpdatedGraphqlEventListenerConfig {

    @Bean
    FluxMessageChannel stockEventsQueue() {

        return new FluxMessageChannel();
    }

}

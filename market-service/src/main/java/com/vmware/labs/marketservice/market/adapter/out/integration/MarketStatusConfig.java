package com.vmware.labs.marketservice.market.adapter.out.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.integration.channel.FluxMessageChannel;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

@Component
public class MarketStatusConfig {

    @Bean
    FluxMessageChannel output() {

        return new FluxMessageChannel();
    }

    @Bean
    public Supplier<Flux<Message<?>>> marketStatus( FluxMessageChannel output ) {

        return () ->
                Flux.from( output );
    }

}

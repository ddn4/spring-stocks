package com.vmware.labs.task.marketStatus.config;

import com.vmware.labs.task.marketStatus.functions.DateToMarketStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Function;

@Configuration
public class MarketStatusConfiguration {

    @Bean
    public Function<Message<?>, Message<?>> dateToMarketStatusConverter() {

        return new DateToMarketStatus();
    }

//    @Bean
//    public Supplier<Flux<Message<?>>> marketStatus() {
//
//        return () -> {
//            LocalDateTime now = LocalDateTime.now();
//            MarketStatusDomainEvent event = new MarketStatusDomainEvent( "OPEN", now );
//
//            log.info( "Market `{}` at [{}]", event.status, event.occurred );
//
//            return Flux.just( MessageBuilder
//                    .withPayload( event )
//                    .build());
//        };
//    }

}

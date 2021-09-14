package com.vmware.labs.task.marketStatus.fn;

import com.vmware.labs.task.marketStatus.MarketStatusDomainEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

@Configuration
@EnableConfigurationProperties( DateToMarketStatusConfigProperties.class )
public class DateToMarketStatus {

    private final DateTimeFormatter formatter;

    private final DateToMarketStatusConfigProperties properties;

    public DateToMarketStatus( final DateToMarketStatusConfigProperties properties ) {

        this.properties = properties;
        this.formatter = DateTimeFormatter.ofPattern( properties.getDateFormat() );
    }

    @Bean
    public Function<Message<?>, Message<?>> dateToMarketStatusConverter() {

        return message -> {

            LocalDateTime received = LocalDateTime.parse( (String) message.getPayload(), formatter );

            return MessageBuilder
                    .withPayload( new MarketStatusDomainEvent( this.properties.getStatus(), received ) )
                    .copyHeaders( message.getHeaders() )
                    .build();
        };
    }

}

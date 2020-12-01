package com.vmware.labs.task.marketStatus.functions;

import com.vmware.labs.task.marketStatus.MarketStatusDomainEvent;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.function.Function;

public class DateToMarketStatus implements Function<Message<?>, Message<?>> {

    @Override
    public Message<?> apply( Message<?> message ) {

        LocalDateTime noon = LocalDateTime.of( LocalDate.now(), LocalTime.NOON );

        LocalDateTime received = LocalDateTime.parse( (String) message.getPayload() );
        String status = received.isBefore( noon ) ? "OPEN" : "CLOSED";

        message = MessageBuilder
                .withPayload( new MarketStatusDomainEvent( status, received ) )
                .copyHeaders( message.getHeaders() )
                .build();

        return message;
    }

}

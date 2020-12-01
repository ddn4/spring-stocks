package com.vmware.labs.task.marketStatus.functions;

import com.vmware.labs.task.marketStatus.MarketStatusDomainEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class DateToMarketStatusTests {

    Function<Message<?>, Message<?>> subject;

    @BeforeEach
    void setup() {

        this.subject = new DateToMarketStatus();

    }

    @Test
    void testMarketStatusOpen() {

        String status = "OPEN";
        LocalDateTime open = LocalDateTime.of( LocalDate.now(), LocalTime.of( 9, 30, 0 ) );

        Message<?> actual = subject.apply( new GenericMessage<>( open.toString() ) );

        MarketStatusDomainEvent expected = new MarketStatusDomainEvent( status, open );

        assertThat( actual )
                .isNotNull()
                .extracting( Message::getPayload )
                    .isEqualTo( expected );
    }

    @Test
    void testMarketStatusClosed() {

        String status = "CLOSED";
        LocalDateTime closed = LocalDateTime.of( LocalDate.now(), LocalTime.of( 16, 0, 0 ) );

        Message<?> actual = subject.apply( new GenericMessage<>( closed.toString() ) );

        MarketStatusDomainEvent expected = new MarketStatusDomainEvent( status, closed );

        assertThat( actual )
                .isNotNull()
                .extracting( Message::getPayload )
                .isEqualTo( expected );
    }

}

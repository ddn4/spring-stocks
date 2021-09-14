package com.vmware.labs.task.marketStatus.fn;

import com.vmware.labs.task.marketStatus.MarketStatusDomainEvent;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

public class DateToMarketStatusTests {

    @Test
    void testMarketStatusOpen() {

        DateToMarketStatusConfigProperties properties = new DateToMarketStatusConfigProperties();
        DateToMarketStatus subject = new DateToMarketStatus( properties );

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern( properties.getDateFormat() );

        LocalDateTime open = LocalDateTime.of( LocalDate.now(), LocalTime.of( 9, 30, 0 ) );

        Message<?> actual = subject.dateToMarketStatusConverter().apply( new GenericMessage<>( open.format( formatter ) ) );

        MarketStatusDomainEvent expected = new MarketStatusDomainEvent( properties.getStatus(), open );

        assertThat( actual )
                .isNotNull()
                .extracting( Message::getPayload )
                    .isEqualTo( expected );
    }

    @Test
    void testMarketStatusClosed() {

        DateToMarketStatusConfigProperties properties = new DateToMarketStatusConfigProperties();
        properties.setStatus( "CLOSED" );
        DateToMarketStatus subject = new DateToMarketStatus( properties );

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern( properties.getDateFormat() );

        LocalDateTime closed = LocalDateTime.of( LocalDate.now(), LocalTime.of( 16, 0, 0 ) );

        Message<?> actual = subject.dateToMarketStatusConverter().apply( new GenericMessage<>( closed.format( formatter ) ) );

        MarketStatusDomainEvent expected = new MarketStatusDomainEvent( properties.getStatus(), closed );

        assertThat( actual )
                .isNotNull()
                .extracting( Message::getPayload )
                .isEqualTo( expected );
    }

}

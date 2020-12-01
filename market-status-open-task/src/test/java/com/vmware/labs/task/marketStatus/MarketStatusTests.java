package com.vmware.labs.task.marketStatus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmware.labs.task.marketStatus.config.MarketStatusConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.cloud.stream.config.BindingServiceConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = {
                "spring.cloud.function.definition=dateToMarketStatusConverter"
        }
)
@DirtiesContext
public class MarketStatusTests {

    @Autowired
    InputDestination inputDestination;

    @Autowired
    OutputDestination outputDestination;

    @Autowired
    ObjectMapper mapper;

    @Test
    void testMarketStatusOpen() throws IOException {

        String status = "OPEN";
        LocalDateTime open = LocalDateTime.of( LocalDate.now(), LocalTime.of( 9, 30, 0 ) );
        inputDestination.send( new GenericMessage<>( open.toString() ) );

        Message<byte[]> sourceMessage = outputDestination.receive(10000 );

        MarketStatusDomainEvent expected = new MarketStatusDomainEvent( status, open );

        assertThat( mapper.readValue( sourceMessage.getPayload(), MarketStatusDomainEvent.class ) )
                .isNotNull()
                .isEqualTo( expected );

    }

    @Test
    void testMarketStatusClosed() throws IOException {

        String status = "CLOSED";
        LocalDateTime closed = LocalDateTime.of( LocalDate.now(), LocalTime.of( 16, 0, 0 ) );
        inputDestination.send( new GenericMessage<>( closed.toString() ) );

        Message<byte[]> sourceMessage = outputDestination.receive(10000 );

        MarketStatusDomainEvent expected = new MarketStatusDomainEvent( status, closed );

        assertThat( mapper.readValue( sourceMessage.getPayload(), MarketStatusDomainEvent.class ) )
                .isNotNull()
                .isEqualTo( expected );

    }

    @SpringBootApplication
    @Import({ MarketStatusConfiguration.class, TestChannelBinderConfiguration.class, BindingServiceConfiguration.class })
    public static class TestApplication { }

}

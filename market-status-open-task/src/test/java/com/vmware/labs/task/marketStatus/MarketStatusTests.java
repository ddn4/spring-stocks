package com.vmware.labs.task.marketStatus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmware.labs.task.marketStatus.fn.DateToMarketStatusConfigProperties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

public class MarketStatusTests {

    @Test
    void testMarketStatusOpen() throws IOException {

        try ( ConfigurableApplicationContext context = new SpringApplicationBuilder(
                TestChannelBinderConfiguration
                        .getCompleteConfiguration( TestApplication.class)
                )
				.web( WebApplicationType.NONE )
                .run(
                        "--spring.cloud.kubernetes.config.enabled=false",
                        "--spring.cloud.function.definition=dateToMarketStatusConverter"
                )

        ) {

            InputDestination inputDestination = context.getBean( InputDestination.class );
            OutputDestination outputDestination = context.getBean( OutputDestination.class );
            ObjectMapper mapper = context.getBean( ObjectMapper.class );

            DateToMarketStatusConfigProperties properties = context.getBean( DateToMarketStatusConfigProperties.class );
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern( properties.getDateFormat() );

            LocalDateTime open = LocalDateTime.of( LocalDate.now(), LocalTime.of( 9, 30, 0 ) );
            inputDestination.send( new GenericMessage<>( open.format( formatter ) ) );

            Message<byte[]> sourceMessage = outputDestination.receive(10000 );

            MarketStatusDomainEvent expected = new MarketStatusDomainEvent( properties.getStatus(), open );

            assertThat( mapper.readValue( sourceMessage.getPayload(), MarketStatusDomainEvent.class ) )
                    .isNotNull()
                    .isEqualTo( expected );

        }

    }

    @Test
    void testMarketStatusClosed() throws IOException {

        try ( ConfigurableApplicationContext context = new SpringApplicationBuilder(
                TestChannelBinderConfiguration
                        .getCompleteConfiguration( TestApplication.class)
        )
                .web( WebApplicationType.NONE )
                .run(
                        "--spring.cloud.kubernetes.discovery.enabled=false",
                        "--spring.cloud.function.definition=dateToMarketStatusConverter",
                        "--date-to-market-status.status=CLOSED"
                )

        ) {

            InputDestination inputDestination = context.getBean( InputDestination.class );
            OutputDestination outputDestination = context.getBean( OutputDestination.class );
            ObjectMapper mapper = context.getBean( ObjectMapper.class );

            DateToMarketStatusConfigProperties properties = context.getBean( DateToMarketStatusConfigProperties.class );
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern( properties.getDateFormat() );

            LocalDateTime closed = LocalDateTime.of( LocalDate.now(), LocalTime.of( 16, 0, 0 ) );
            inputDestination.send( new GenericMessage<>( closed.format( formatter ) ) );

            Message<byte[]> sourceMessage = outputDestination.receive(10000 );

            MarketStatusDomainEvent expected = new MarketStatusDomainEvent( properties.getStatus(), closed );

            assertThat( mapper.readValue( sourceMessage.getPayload(), MarketStatusDomainEvent.class ) )
                    .isNotNull()
                    .isEqualTo( expected );

        }

    }

    @SpringBootApplication
    public static class TestApplication { }

}

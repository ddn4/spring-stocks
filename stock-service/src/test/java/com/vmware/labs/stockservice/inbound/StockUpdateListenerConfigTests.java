package com.vmware.labs.stockservice.inbound;

import com.vmware.labs.stockservice.applicationevents.StockUpdateEvent;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.support.GenericMessage;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(
        classes = { StockUpdateListenerConfig.class, StockUpdateListenerConfigTests.TestConfig.class },
        properties = {
                "spring.cloud.kubernetes.discovery.enabled=false",
                "spring.stocks.seeder.enabled=false"
        }
)
class StockUpdateListenerConfigTests {

    @Autowired
    private InputDestination input;

    @Test
    void testReceiveStockUpdateEvent() {

        String json = "{\"symbol\":\"test\", \"price\":1.00}";
        this.input.send( new GenericMessage<>( json.getBytes() ) );

    }

    @SpringBootApplication
    @Import( TestChannelBinderConfiguration.class )
    static class TestConfig {

        String fakeSymbol = "test";
        BigDecimal fakePrice = new BigDecimal( "1.00" );

        @EventListener
        public void handleEvent( StockUpdateEvent event ) {
            log.info( "handleEvent : event = {}", event );

            assertThat( event.getSymbol() ).isEqualTo( fakeSymbol );
            assertThat( event.getPrice() ).isEqualTo( fakePrice );

        }

    }

}
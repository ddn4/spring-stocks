package com.vmware.labs.stockService.inbound;

import com.vmware.labs.stockService.applicationEvents.StockUpdateEvent;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ExtendWith( SpringExtension.class )
@SpringBootTest(
        classes = { StockUpdateListenerConfig.class, StockUpdateListenerConfigTests.TestConfig.class },
        properties = {
                "spring.cloud.kubernetes.enabled=false",
                "spring.stocks.seeder.enabled=false"
        }
)
class StockUpdateListenerConfigTests {

    @Autowired
    private InputDestination input;

    @Test
    public void testReceiveStockUpdateEvent() {

        String json = "{\"symbol\":\"test\", \"price\":1.00}";
        this.input.send( new GenericMessage<byte[]>( json.getBytes() ) );

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
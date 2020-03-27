package com.vmware.labs.stockService.stock.adapter.in.endpoint;

import com.vmware.labs.stockService.applicationEvents.StockUpdateEvent;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.event.EventListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ExtendWith( SpringExtension.class )
@ContextConfiguration( classes = { StockRouter.class, StockHandler.class, StockRoutesTests.TestConfig.class })
@WebFluxTest
class StockRoutesTests {

    @Autowired
    RouterFunction<ServerResponse> stockRoutes;

    WebTestClient webTestClient;

    String fakeSymbol = "test";
    BigDecimal fakePrice = new BigDecimal( "1.00" );

    @BeforeEach
    public void setUp() {

        webTestClient = WebTestClient.bindToRouterFunction( stockRoutes ).build();

    }

    @Test
    public void testPutPrice() {

        this.webTestClient.put()
                .uri(  uriBuilder -> uriBuilder
                        .path( "/api/stocks/{symbol}" )
                        .queryParam( "price", fakePrice )
                        .build( fakeSymbol ) )
                .exchange()
                    .expectStatus().isAccepted();

    }

    @TestConfiguration
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
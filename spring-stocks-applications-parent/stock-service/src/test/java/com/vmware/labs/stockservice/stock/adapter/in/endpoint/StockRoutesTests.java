package com.vmware.labs.stockservice.stock.adapter.in.endpoint;

import com.vmware.labs.stockservice.applicationevents.StockUpdateEvent;
import com.vmware.labs.stockservice.stock.application.RetrieveStockService;
import com.vmware.labs.stockservice.stock.application.out.LookupStockProjectionPort;
import com.vmware.labs.stockservice.stock.domain.StockProjection;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.event.EventListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@Slf4j
@ContextConfiguration( classes = { RetrieveStockService.class, StockRouter.class, StockHandler.class, StockRoutesTests.TestConfig.class })
@WebFluxTest
class StockRoutesTests {

    @Autowired
    RouterFunction<ServerResponse> stockRoutes;

    @MockBean
    LookupStockProjectionPort mockLookupStockProjectionPort;

    WebTestClient webTestClient;

    String fakeSymbol = "test";
    BigDecimal fakePrice = new BigDecimal( "1.00" );
    Instant fakeOccurredOn = Instant.now();

    @BeforeEach
    public void setUp() {

        webTestClient = WebTestClient.bindToRouterFunction( stockRoutes ).build();

    }

    @Test
    void testRetrievePrice() {

        when( this.mockLookupStockProjectionPort.lookupBySymbol( fakeSymbol ) ).thenReturn( Mono.just( new StockProjection( fakeSymbol, fakePrice, fakeOccurredOn ) ) );

        this.webTestClient.get()
                .uri(  uriBuilder -> uriBuilder
                        .path( "/api/stocks/{symbol}" )
                        .build( fakeSymbol ) )
                .exchange()
                    .expectStatus().isOk()
                    .expectBody( StockRestView.class );

        verify( this.mockLookupStockProjectionPort).lookupBySymbol( fakeSymbol );
        verifyNoMoreInteractions( this.mockLookupStockProjectionPort);

    }

    @Test
    void testPutPrice() {

        this.webTestClient.put()
                .uri(  uriBuilder -> uriBuilder
                        .path( "/api/stocks/{symbol}" )
                        .queryParam( "price", fakePrice )
                        .build( fakeSymbol ) )
                .exchange()
                    .expectStatus().isAccepted();

        verifyNoInteractions( this.mockLookupStockProjectionPort);

    }

    @TestConfiguration
    static class TestConfig {

        String fakeSymbol = "test";
        BigDecimal fakePrice = new BigDecimal( "1.00" );

        @EventListener
        public void handleEvent( StockUpdateEvent event ) {
            log.info( "handleEvent : event = {}", event );

            assertThat( event.symbol() ).isEqualTo( fakeSymbol );
            assertThat( event.price() ).isEqualTo( fakePrice );

        }

    }
}

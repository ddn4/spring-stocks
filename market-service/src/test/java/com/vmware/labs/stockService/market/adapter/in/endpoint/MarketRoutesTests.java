package com.vmware.labs.stockService.market.adapter.in.endpoint;

import com.vmware.labs.stockService.market.application.CloseMarketService;
import com.vmware.labs.stockService.market.application.GetMarketStatusService;
import com.vmware.labs.stockService.market.application.OpenMarketService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.Map;

@Slf4j
@ExtendWith( SpringExtension.class )
@ContextConfiguration( classes = { OpenMarketService.class, CloseMarketService.class, GetMarketStatusService.class, MarketRouter.class, MarketHandler.class })
@WebFluxTest
public class MarketRoutesTests {

    @Autowired
    RouterFunction<ServerResponse> marketRoutes;

    WebTestClient webTestClient;

    @BeforeEach
    public void setUp() {

        webTestClient = WebTestClient.bindToRouterFunction( marketRoutes ).build();

    }

    @Test
    public void testOpenMarket() {

        this.webTestClient.put()
                .uri( uriBuilder -> uriBuilder
                        .path( "/api/market/open" )
                        .build() )
                .exchange()
                    .expectStatus().isAccepted();
    }

    @Test
    public void testCloseMarket() {

        this.webTestClient.put()
                .uri( uriBuilder -> uriBuilder
                        .path( "/api/market/close" )
                        .build() )
                .exchange()
                .expectStatus().isAccepted();
    }

    @Test
    public void testGetMarketStatus() {

        this.webTestClient.get()
                .uri( uriBuilder -> uriBuilder
                        .path( "/api/market" )
                        .build() )
                .exchange()
                    .expectStatus().isOk()
                    .expectBody( new ParameterizedTypeReference<Map<String, Object>>() {} )
                        .isEqualTo( Map.of( "marketStatus", "OPEN" ) );
    }

}

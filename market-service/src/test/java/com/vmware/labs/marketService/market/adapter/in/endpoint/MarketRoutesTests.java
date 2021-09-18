package com.vmware.labs.marketService.market.adapter.in.endpoint;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.time.LocalDateTime;
import java.util.Map;

import static com.vmware.labs.marketService.market.application.MarketStatus.OPEN;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.accepted;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Slf4j
@WebFluxTest
@Import( MarketRouter.class )
public class MarketRoutesTests {

    @Autowired
    RouterFunction<ServerResponse> marketRoutes;

    @MockBean
    MarketHandler mockMarketHandler;

    WebTestClient webTestClient;

    @BeforeEach
    public void setUp() {

        webTestClient =
                WebTestClient
                        .bindToRouterFunction( marketRoutes )
                        .build();

    }

    @Test
    public void testOpenMarket() {

        when( this.mockMarketHandler.openMarket( any( ServerRequest.class ) ) ).thenReturn( accepted().build() );

        this.webTestClient.put()
                .uri( uriBuilder -> uriBuilder
                        .path( "/api/market/open" )
                        .build() )
                .exchange()
                    .expectStatus().isAccepted();

        verify( this.mockMarketHandler ).openMarket( any( ServerRequest.class ) );

    }

    @Test
    public void testCloseMarket() {

        when( this.mockMarketHandler.closeMarket( any( ServerRequest.class ) ) ).thenReturn( accepted().build() );

        this.webTestClient.put()
                .uri( uriBuilder -> uriBuilder
                        .path( "/api/market/close" )
                        .build() )
                .exchange()
                .expectStatus().isAccepted();

        verify( this.mockMarketHandler ).closeMarket( any( ServerRequest.class ) );

    }

    @Test
    public void testGetMarketStatus() {

        LocalDateTime fakeOccurred = LocalDateTime.now();
        var fakeResponse = Map.of( "marketStatus", OPEN.name(), "occurred", fakeOccurred.toString() );
        var fakeParameterizedTypeReference = new ParameterizedTypeReference<Map<String, Object>>() {};
        var fakeResponseBody = ok().contentType( APPLICATION_JSON ).bodyValue( fakeResponse );
        when( this.mockMarketHandler.retrieveMarketStatus( any( ServerRequest.class ) ) ).thenReturn( fakeResponseBody );

        this.webTestClient.get()
                .uri( uriBuilder -> uriBuilder
                        .path( "/api/market" )
                        .build() )
                .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType( APPLICATION_JSON )
                    .expectBody( fakeParameterizedTypeReference )
                        .isEqualTo( Map.of( "marketStatus", "OPEN", "occurred", fakeOccurred.toString() ) );

        verify( this.mockMarketHandler ).retrieveMarketStatus( any( ServerRequest.class ) );

    }

}

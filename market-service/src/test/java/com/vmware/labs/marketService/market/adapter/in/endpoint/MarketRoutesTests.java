package com.vmware.labs.marketService.market.adapter.in.endpoint;

import com.vmware.labs.marketService.market.application.CloseMarketService;
import com.vmware.labs.marketService.market.application.CurrentMarketStatus;
import com.vmware.labs.marketService.market.application.GetMarketStatusService;
import com.vmware.labs.marketService.market.application.OpenMarketService;
import com.vmware.labs.marketService.market.application.out.GetMarketStatusPort;
import com.vmware.labs.marketService.market.application.out.UpdateMarketStatusPort;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.time.LocalDateTime;
import java.util.Map;

import static com.vmware.labs.marketService.market.application.MarketStatus.OPEN;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith( SpringExtension.class )
@ContextConfiguration( classes = { OpenMarketService.class, CloseMarketService.class, GetMarketStatusService.class, MarketRouter.class, MarketHandler.class })
@WebFluxTest
public class MarketRoutesTests {

    @Autowired
    RouterFunction<ServerResponse> marketRoutes;

    @MockBean
    GetMarketStatusPort mockGetMarketStatusPort;

    @MockBean
    UpdateMarketStatusPort mockUpdateMarketStatusPort;

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

        LocalDateTime fakeOccurred = LocalDateTime.now();
        when( this.mockGetMarketStatusPort.currentStatus() ).thenReturn( new CurrentMarketStatus( OPEN, fakeOccurred ) );

        this.webTestClient.get()
                .uri( uriBuilder -> uriBuilder
                        .path( "/api/market" )
                        .build() )
                .exchange()
                    .expectStatus().isOk()
                    .expectBody( new ParameterizedTypeReference<Map<String, Object>>() {} )
                        .isEqualTo( Map.of( "marketStatus", "OPEN", "occurred", fakeOccurred.toString() ) );
    }

}

package com.vmware.labs.stockService.stock.adapter.in.endpoint;

import com.vmware.labs.stockService.applicationEvents.StockUpdateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.springframework.web.reactive.function.server.ServerResponse.accepted;

@Slf4j
@Component
@RequiredArgsConstructor
class StockHandler {

    private final ApplicationEventPublisher applicationEventPublisher;

    public Mono<ServerResponse> updateStock( ServerRequest request ) {
        log.info( "updateStock : enter" );

        String symbol = request.pathVariable( "symbol" );
        BigDecimal price = request.queryParam( "price" ).map( BigDecimal::new ).get();

        this.applicationEventPublisher.publishEvent( new StockUpdateEvent( this, symbol, price ) );

        log.info( "updateStock : exit" );
        return accepted().build();
    }

}

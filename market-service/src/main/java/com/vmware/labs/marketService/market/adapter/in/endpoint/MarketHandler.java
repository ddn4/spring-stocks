package com.vmware.labs.marketService.market.adapter.in.endpoint;

import com.vmware.labs.marketService.market.application.in.OpenMarketUseCase;
import com.vmware.labs.marketService.market.application.in.CloseMarketUseCase;
import com.vmware.labs.marketService.market.application.in.CloseMarketUseCase.CloseMarketCommand;
import com.vmware.labs.marketService.market.application.in.GetMarketStatusQuery;
import com.vmware.labs.marketService.market.application.in.GetMarketStatusQuery.GetMarketStatusCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.accepted;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Slf4j
@Component
@RequiredArgsConstructor
class MarketHandler {

    private final OpenMarketUseCase openMarketUseCase;
    private final CloseMarketUseCase closeMarketUseCase;
    private final GetMarketStatusQuery getMarketStatusQuery;

    public Mono<ServerResponse> openMarket( ServerRequest request ) {
        log.info( "openMarket: enter" );

        this.openMarketUseCase.execute( new OpenMarketUseCase.OpenMarketCommand() );

        return accepted().build();
    }

    public Mono<ServerResponse> closeMarket( ServerRequest request ) {
        log.info( "closeMarket: enter" );

        this.closeMarketUseCase.execute( new CloseMarketCommand() );
        return accepted().build();
    }

    public Mono<ServerResponse> retrieveMarketStatus( ServerRequest request ) {
        log.info( "retrieveMarketStatus : enter" );

        log.info( "retrieveMarketStatus : exit" );
        return ok()
                .body( this.getMarketStatusQuery.execute( new GetMarketStatusCommand() ), new ParameterizedTypeReference<>() {} );
    }

}

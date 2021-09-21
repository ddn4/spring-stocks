package com.vmware.labs.marketservice.market.adapter.in.endpoint;

import com.vmware.labs.marketservice.market.application.in.OpenMarketUseCase;
import com.vmware.labs.marketservice.market.application.in.CloseMarketUseCase;
import com.vmware.labs.marketservice.market.application.in.CloseMarketUseCase.CloseMarketCommand;
import com.vmware.labs.marketservice.market.application.in.GetMarketStatusQuery;
import com.vmware.labs.marketservice.market.application.in.GetMarketStatusQuery.GetMarketStatusCommand;
import com.vmware.labs.marketservice.market.application.in.OpenMarketUseCase.OpenMarketCommand;
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

    /**
     *
     * @param request <code>ServerRequest</code> is required for server handlers
     * @return <code>Mono<ServerResponse></code>
     */
    public Mono<ServerResponse> openMarket( ServerRequest request ) {
        log.info( "openMarket: enter" );

        return this.openMarketUseCase.execute( new OpenMarketCommand() )
                .flatMap( state -> accepted().build() );
    }

    /**
     *
     * @param request <code>ServerRequest</code> is required for server handlers
     * @return <code>Mono<ServerResponse></code>
     */
    public Mono<ServerResponse> closeMarket( ServerRequest request ) {
        log.info( "closeMarket: enter" );

        return this.closeMarketUseCase.execute( new CloseMarketCommand() )
                .flatMap( state -> accepted().build() );
    }

    /**
     *
     * @param request <code>ServerRequest</code> is required for server handlers
     * @return <code>Mono<ServerResponse></code>
     */
    public Mono<ServerResponse> retrieveMarketStatus( ServerRequest request ) {
        log.info( "retrieveMarketStatus : enter" );

        log.info( "retrieveMarketStatus : exit" );
        return ok()
                .body( this.getMarketStatusQuery.execute( new GetMarketStatusCommand() ), new ParameterizedTypeReference<>() {} );
    }

}

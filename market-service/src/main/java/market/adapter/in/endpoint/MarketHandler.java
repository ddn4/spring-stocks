package market.adapter.in.endpoint;

import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties.Web.Server.ServerRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import market.application.in.CloseMarketUseCase;
import market.application.in.OpenMarketUseCase;
import market.application.in.CloseMarketUseCase.CloseMarketCommand;
import market.application.in.OpenMarketUseCase.OpenMarketCommand;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.accepted;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Slf4j
@Component
@RequiredArgsConstructor
class MarketHandler {

    private final OpenMarketUseCase openMarketUseCase;
    private final CloseMarketUseCase closeMarketUseCase;

    public Mono<ServerResponse> openMarket( ServerRequest request ) {
        log.info( "openMarket: enter" );

        this.openMarketUseCase.execute(new OpenMarketCommand());

        return accepted().build();
    }

    public Mono<ServerResponse> closeMarket( ServerRequest request ) {
        log.info( "closeMarket: enter" );

        this.closeMarketUseCase.execute(new CloseMarketCommand());
        return accepted().build();
    }

    public Mono<ServerResponse> retrieveMarketStatus( ServerRequest request ) {
        log.info( "retrieveMarketStatus : enter" );


        log.info( "retrieveMarketStatus : exit" );
        return ok().build();
    }
}

package com.vmware.labs.marketservice.market.application.in;

import com.vmware.labs.marketservice.applicationevents.MarketClosedEvent;
import com.vmware.labs.marketservice.common.usecase.UseCase;
import com.vmware.labs.marketservice.market.application.MarketStatusState;
import com.vmware.labs.marketservice.market.application.out.UpdateMarketStatusPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import reactor.core.publisher.Mono;

import static com.vmware.labs.marketservice.market.application.MarketStatus.CLOSED;

@Slf4j
@UseCase
@RequiredArgsConstructor
public final class CloseMarketService implements CloseMarketUseCase {

    private final UpdateMarketStatusPort updateMarketStatusPort;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public Mono<MarketStatusState> execute( final CloseMarketCommand command ) {

        return this.updateMarketStatusPort.setCurrentStatus( CLOSED, command.getTimeClosed() )
                .doOnNext( state ->
                        this.applicationEventPublisher
                                .publishEvent( new MarketClosedEvent( state.occurred() ) )
                );

    }

}

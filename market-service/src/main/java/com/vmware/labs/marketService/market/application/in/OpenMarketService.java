package com.vmware.labs.marketService.market.application.in;

import com.vmware.labs.marketService.applicationEvents.MarketOpenedEvent;
import com.vmware.labs.marketService.common.useCase.UseCase;
import com.vmware.labs.marketService.market.application.MarketStatusState;
import com.vmware.labs.marketService.market.application.out.UpdateMarketStatusPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import reactor.core.publisher.Mono;

import static com.vmware.labs.marketService.market.application.MarketStatus.OPEN;

@Slf4j
@UseCase
@RequiredArgsConstructor
public final class OpenMarketService implements OpenMarketUseCase {

    private final UpdateMarketStatusPort updateMarketStatusPort;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public Mono<MarketStatusState> execute( final OpenMarketCommand command ) {

        return this.updateMarketStatusPort.setCurrentStatus( OPEN, command.getTimeOpened() )
                .doOnNext( state ->
                        this.applicationEventPublisher
                                .publishEvent( new MarketOpenedEvent( this, state.occurred() ) )
                );

    }

}

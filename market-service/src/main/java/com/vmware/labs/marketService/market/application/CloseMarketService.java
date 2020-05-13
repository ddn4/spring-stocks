package com.vmware.labs.marketService.market.application;

import com.vmware.labs.marketService.applicationEvents.MarketClosedEvent;
import com.vmware.labs.marketService.common.useCase.UseCase;
import com.vmware.labs.marketService.market.application.in.CloseMarketUseCase;
import com.vmware.labs.marketService.market.application.out.UpdateMarketStatusPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;

import static com.vmware.labs.marketService.market.application.MarketStatus.CLOSED;

@Slf4j
@UseCase
@RequiredArgsConstructor
public class CloseMarketService implements CloseMarketUseCase {

    private final UpdateMarketStatusPort updateMarketStatusPort;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void execute( CloseMarketCommand command ) {

        this.updateMarketStatusPort.setCurrentStatus( CLOSED, command.getTimeClosed() );

        this.applicationEventPublisher.publishEvent( new MarketClosedEvent( this, command.getTimeClosed() ) );

    }

}

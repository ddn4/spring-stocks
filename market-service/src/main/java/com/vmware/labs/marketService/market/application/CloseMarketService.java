package com.vmware.labs.marketService.market.application;

import com.vmware.labs.marketService.applicationEvents.MarketClosedEvent;
import com.vmware.labs.marketService.common.useCase.UseCase;
import com.vmware.labs.marketService.market.application.in.CloseMarketUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;

@Slf4j
@UseCase
@RequiredArgsConstructor
public class CloseMarketService implements CloseMarketUseCase {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void execute( CloseMarketCommand command ) {

        this.applicationEventPublisher.publishEvent( new MarketClosedEvent( this, command.getTimeClosed() ) );

    }

}

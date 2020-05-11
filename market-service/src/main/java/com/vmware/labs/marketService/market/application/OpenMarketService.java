package com.vmware.labs.marketService.market.application;

import com.vmware.labs.marketService.applicationEvents.MarketOpenedEvent;
import com.vmware.labs.marketService.common.useCase.UseCase;
import com.vmware.labs.marketService.market.application.in.OpenMarketUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;

@Slf4j
@UseCase
@RequiredArgsConstructor
public class OpenMarketService implements OpenMarketUseCase {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void execute( OpenMarketCommand command ) {

        this.applicationEventPublisher.publishEvent( new MarketOpenedEvent( this, command.getTimeOpened() ) );

    }

}

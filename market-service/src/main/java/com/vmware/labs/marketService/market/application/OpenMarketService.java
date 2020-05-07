package com.vmware.labs.marketService.market.application;

import com.vmware.labs.marketService.common.useCase.UseCase;
import com.vmware.labs.marketService.market.application.in.OpenMarketUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UseCase
@RequiredArgsConstructor
public class OpenMarketService implements OpenMarketUseCase {

    @Override
    public void execute( OpenMarketCommand command ) {

    }

}

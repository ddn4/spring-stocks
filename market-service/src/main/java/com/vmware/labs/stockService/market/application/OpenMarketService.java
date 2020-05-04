package com.vmware.labs.stockService.market.application;

import com.vmware.labs.stockService.common.useCase.UseCase;
import com.vmware.labs.stockService.market.application.in.OpenMarketUseCase;
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

package com.vmware.labs.stockService.market.application;

import com.vmware.labs.stockService.common.useCase.UseCase;
import com.vmware.labs.stockService.market.application.in.CloseMarketUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UseCase
@RequiredArgsConstructor
public class CloseMarketService implements CloseMarketUseCase {

    @Override
    public void execute( CloseMarketCommand command ) {

    }

}

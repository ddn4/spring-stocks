package com.vmware.labs.marketService.market.application;

import com.vmware.labs.marketService.common.useCase.UseCase;
import com.vmware.labs.marketService.market.application.in.CloseMarketUseCase;
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

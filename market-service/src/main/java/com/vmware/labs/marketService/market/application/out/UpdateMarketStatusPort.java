package com.vmware.labs.marketService.market.application.out;

import com.vmware.labs.marketService.market.application.MarketStatus;
import com.vmware.labs.marketService.market.application.MarketStatusState;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface UpdateMarketStatusPort {

    Mono<MarketStatusState> setCurrentStatus( final MarketStatus marketStatus, final LocalDateTime occurred );

}

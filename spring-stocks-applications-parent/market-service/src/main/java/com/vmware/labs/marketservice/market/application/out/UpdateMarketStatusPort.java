package com.vmware.labs.marketservice.market.application.out;

import com.vmware.labs.marketservice.market.application.MarketStatus;
import com.vmware.labs.marketservice.market.application.MarketStatusState;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface UpdateMarketStatusPort {

    Mono<MarketStatusState> setCurrentStatus( final MarketStatus marketStatus, final LocalDateTime occurred );

}

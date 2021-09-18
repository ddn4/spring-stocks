package com.vmware.labs.marketService.market.application.out;

import com.vmware.labs.marketService.market.application.CurrentMarketStatus;
import reactor.core.publisher.Mono;

public interface GetMarketStatusPort {

    Mono<CurrentMarketStatus> currentStatus();

}

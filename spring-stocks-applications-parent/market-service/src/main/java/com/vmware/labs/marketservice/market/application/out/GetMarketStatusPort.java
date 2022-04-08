package com.vmware.labs.marketservice.market.application.out;

import com.vmware.labs.marketservice.market.application.CurrentMarketStatus;
import reactor.core.publisher.Mono;

public interface GetMarketStatusPort {

    Mono<CurrentMarketStatus> currentStatus();

}

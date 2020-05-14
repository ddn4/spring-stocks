package com.vmware.labs.marketService.market.application.out;

import com.vmware.labs.marketService.market.application.CurrentMarketStatus;

public interface GetMarketStatusPort {

    CurrentMarketStatus currentStatus();

}

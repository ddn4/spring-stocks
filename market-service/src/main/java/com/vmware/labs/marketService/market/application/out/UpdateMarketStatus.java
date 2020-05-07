package com.vmware.labs.marketService.market.application.out;

import com.vmware.labs.marketService.market.application.MarketStatus;

public interface UpdateMarketStatus {

    void setCurrentStatus( MarketStatus marketStatus );

}

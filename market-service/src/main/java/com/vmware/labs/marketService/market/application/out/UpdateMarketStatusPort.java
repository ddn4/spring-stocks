package com.vmware.labs.marketService.market.application.out;

import com.vmware.labs.marketService.market.application.MarketStatus;

import java.time.LocalDateTime;

public interface UpdateMarketStatusPort {

    void setCurrentStatus( MarketStatus marketStatus, LocalDateTime occurred );

}

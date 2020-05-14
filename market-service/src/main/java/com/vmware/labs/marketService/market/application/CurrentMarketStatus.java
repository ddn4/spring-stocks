package com.vmware.labs.marketService.market.application;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CurrentMarketStatus {

    private final MarketStatus status;
    private final LocalDateTime occurred;

}

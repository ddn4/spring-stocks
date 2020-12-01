package com.vmware.labs.marketService.market.adapter.out.integration;

import lombok.Data;

import java.time.LocalDateTime;

@Data
class MarketStatusDomainEvent {

    private final String status;
    private final LocalDateTime occurred;

}

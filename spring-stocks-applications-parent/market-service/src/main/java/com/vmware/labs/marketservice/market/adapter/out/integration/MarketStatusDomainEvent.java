package com.vmware.labs.marketservice.market.adapter.out.integration;

import java.time.LocalDateTime;

record MarketStatusDomainEvent(
        String status,
        LocalDateTime occurred
) { }

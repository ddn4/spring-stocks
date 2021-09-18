package com.vmware.labs.marketService.market.adapter.out.integration;

import java.time.LocalDateTime;

record MarketStatusDomainEvent(
        String status,
        LocalDateTime occurred
) { }

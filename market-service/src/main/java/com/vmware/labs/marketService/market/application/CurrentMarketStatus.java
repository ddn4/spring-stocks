package com.vmware.labs.marketService.market.application;

import java.time.LocalDateTime;

public record CurrentMarketStatus(
        MarketStatus status,
        LocalDateTime occurred
) {}

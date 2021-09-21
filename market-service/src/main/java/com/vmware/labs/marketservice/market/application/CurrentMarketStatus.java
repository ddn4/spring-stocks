package com.vmware.labs.marketservice.market.application;

import java.time.LocalDateTime;

public record CurrentMarketStatus(
        MarketStatus status,
        LocalDateTime occurred
) {}

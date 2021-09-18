package com.vmware.labs.marketService.market.application;

import java.time.LocalDateTime;
import java.util.UUID;

public record MarketStatusState(
        UUID id,
        MarketStatus marketStatus,
        LocalDateTime occurred
) {}

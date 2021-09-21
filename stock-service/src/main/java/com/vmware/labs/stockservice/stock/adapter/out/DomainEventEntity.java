package com.vmware.labs.stockservice.stock.adapter.out;

import java.time.Instant;
import java.util.UUID;

record DomainEventEntity(
        UUID id,
        String symbol,
        Instant occurredOn,
        String event
) {

}

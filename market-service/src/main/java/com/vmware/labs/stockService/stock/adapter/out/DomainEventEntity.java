package com.vmware.labs.stockService.stock.adapter.out;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
class DomainEventEntity {

    private final UUID id;
    private final String symbol;
    private final Instant occurredOn;
    private final String event;

}

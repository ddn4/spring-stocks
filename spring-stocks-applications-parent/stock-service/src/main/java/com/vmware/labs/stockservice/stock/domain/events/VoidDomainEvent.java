package com.vmware.labs.stockservice.stock.domain.events;

import java.time.Instant;

public final class VoidDomainEvent implements DomainEvent {

    @Override
    public String symbol() {
        return null;
    }

    @Override
    public Instant occurredOn() {
        return null;
    }

    @Override
    public String type() {
        return null;
    }
    
}
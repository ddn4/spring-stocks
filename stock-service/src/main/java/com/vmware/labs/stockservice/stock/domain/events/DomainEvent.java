package com.vmware.labs.stockservice.stock.domain.events;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.Instant;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        property = "type",
        defaultImpl = VoidDomainEvent.class
)
@JsonSubTypes({
        @JsonSubTypes.Type( name = "PriceChanged", value = PriceChanged.class )
})
public sealed interface DomainEvent permits VoidDomainEvent, PriceChanged {

    String symbol();

    Instant occurredOn();

    String type();

}
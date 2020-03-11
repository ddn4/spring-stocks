package com.vmware.labs.stockService.stock.domain.events;

import java.time.Instant;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(name = PriceChanged.TYPE, value = PriceChanged.class )
})
public interface DomainEvent {

    String symbol();

    Instant when();

    String type();

}
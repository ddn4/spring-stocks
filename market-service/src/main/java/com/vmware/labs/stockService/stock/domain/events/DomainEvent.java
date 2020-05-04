package com.vmware.labs.stockService.stock.domain.events;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.Instant;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type( name = "PriceChanged", value = PriceChanged.class )
})
public interface DomainEvent {

    String symbol();

    Instant when();

    String type();

}
package com.vmware.labs.stockservice.stock.adapter.out.persistence;

import com.vmware.labs.stockservice.stock.domain.events.DomainEvent;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

@Table( "stock_domain_events" )
record DomainEventEntity(
        @Id UUID id,
        @Version Long version,
        @Column @NotEmpty String symbol,
        @Column( "occurred" ) @NotNull Instant occurredOn,
        @Column @NotEmpty DomainEvent event
) {

}

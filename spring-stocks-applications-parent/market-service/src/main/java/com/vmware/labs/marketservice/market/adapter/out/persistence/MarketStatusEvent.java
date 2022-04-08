package com.vmware.labs.marketservice.market.adapter.out.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Table( "market_status_events" )
record MarketStatusEvent(
        @Id UUID id,
        @Version Long version,
        @Column @NotEmpty String status,
        @Column @NotNull LocalDateTime occurred
) {

}

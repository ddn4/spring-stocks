package com.vmware.labs.marketservice.market.adapter.out.persistence;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Table( "market_status_events" )
@Data
@NoArgsConstructor
class MarketStatusEvent {

    @Id
    private String id;

    @Version
    private Long version;

    @Column
    @NotEmpty
    private String status;

    @Column
    @NotNull
    private LocalDateTime occurred;

}

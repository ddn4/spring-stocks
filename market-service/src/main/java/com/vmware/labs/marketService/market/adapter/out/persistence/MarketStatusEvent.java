package com.vmware.labs.marketService.market.adapter.out.persistence;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity( name = "market_status_events" )
@Data
@NoArgsConstructor
class MarketStatusEvent {

    @Id
    @GeneratedValue( generator = "UUID" )
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @ColumnDefault( "random_uuid()" )
    @Type( type = "uuid-char" )
    private UUID id;

    @Column
    @NotEmpty
    private String status;

    @Column
    @NotNull
    private LocalDateTime occurred;

}

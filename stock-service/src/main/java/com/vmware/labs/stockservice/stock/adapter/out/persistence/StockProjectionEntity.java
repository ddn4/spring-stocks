package com.vmware.labs.stockservice.stock.adapter.out.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Table( "stock_projections" )
record StockProjectionEntity(
        @Id UUID id,
        @Version Long version,
        @Column @NotEmpty String symbol,
        @Column @NotNull BigDecimal price,
        @Column @NotNull Instant lastPriceChanged
) {

}

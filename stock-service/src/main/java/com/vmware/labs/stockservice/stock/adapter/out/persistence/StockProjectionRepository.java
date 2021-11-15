package com.vmware.labs.stockservice.stock.adapter.out.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface StockProjectionRepository extends ReactiveCrudRepository<StockProjectionEntity, UUID> {

    Mono<StockProjectionEntity> findBySymbol( String sybmol );

}

package com.vmware.labs.stockservice.stock.adapter.out.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface StockDomainEventRepository extends ReactiveCrudRepository<DomainEventEntity, UUID> {

    Flux<DomainEventEntity> findAllBySymbol( String symnol );

}

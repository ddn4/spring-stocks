package com.vmware.labs.marketService.market.adapter.out.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

interface MarketStatusEventRepository extends ReactiveCrudRepository<MarketStatusEvent, UUID> {

    Mono<MarketStatusEvent> findTop1ByOccurredAfterOrderByOccurredDesc( LocalDateTime startOfDay );

}

package com.vmware.labs.marketService.market.adapter.out.persistence;

import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

interface MarketStatusEventRepository extends CrudRepository<MarketStatusEvent, UUID> {

    List<MarketStatusEvent> findAllByOccurredAfter(LocalDateTime startOfDay );

}

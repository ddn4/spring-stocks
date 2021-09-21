package com.vmware.labs.stockservice.stock.adapter.out;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.List.of;

@Slf4j
@Component
class InMemoryStockEventsDataStore implements StockEventsDataStore {

    private final Map<String, List<DomainEventEntity>> data = new ConcurrentHashMap<>();

    @Override
    public Flux<DomainEventEntity> findBySymbol( final String symbol ) {
        log.debug( "findBySymbol : looking up stock for symbol '{}'", symbol );

        return Flux.fromStream( this.data.getOrDefault( symbol, of() ).stream() );
    }

    @Override
    public Mono<DomainEventEntity> save( final DomainEventEntity event ) {

        List<DomainEventEntity> events = this.data.getOrDefault( event.symbol(), new ArrayList<>() );
        events.add( event );
        this.data.put( event.symbol(), events );
        log.debug( "save : saved stock event for symbol '{} {}'", event.symbol(), event );

        return Mono.just( event );
    }

}

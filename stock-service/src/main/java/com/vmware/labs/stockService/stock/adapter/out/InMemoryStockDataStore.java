package com.vmware.labs.stockService.stock.adapter.out;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.List.of;

@Slf4j
@Component
class InMemoryStockDataStore implements StockDataStore {

    private Map<String, List<DomainEventEntity>> data = new ConcurrentHashMap<>();

    @Override
    public List<DomainEventEntity> findBySymbol(final String symbol ) {
        log.debug( "findBySymbol : looking up stock for symbol '{}'", symbol );

        return this.data.getOrDefault( symbol, of() );
    }

    @Override
    public void save( DomainEventEntity event ) {

        List<DomainEventEntity> events = data.getOrDefault( event.getSymbol(), new ArrayList<>() );
        events.add( event );
        data.put( event.getSymbol(), events );
        log.debug( "save : saved stock event for symbol '{} {}'", event.getSymbol(), event );

    }

}

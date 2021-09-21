package com.vmware.labs.stockservice.stock.adapter.out;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmware.labs.stockservice.common.persistence.PersistenceAdapter;
import com.vmware.labs.stockservice.common.usecase.UuidGenerator;
import com.vmware.labs.stockservice.stock.application.out.GetStockEventsPort;
import com.vmware.labs.stockservice.stock.application.out.LookupStockPort;
import com.vmware.labs.stockservice.stock.application.out.PersistStockEventPort;
import com.vmware.labs.stockservice.stock.application.out.PutStockCachePort;
import com.vmware.labs.stockservice.stock.domain.StockCache;
import com.vmware.labs.stockservice.stock.domain.events.DomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@PersistenceAdapter
@RequiredArgsConstructor
public class StockPersistenceAdapter implements GetStockEventsPort, PersistStockEventPort, PutStockCachePort, LookupStockPort {

    private final StockEventsDataStore stockEventsDataStore;
    private final StockCacheDataStore stockCacheDataStore;
    private final UuidGenerator uuidGenerator;
    private final ObjectMapper mapper;

    @Override
    public Flux<DomainEvent> getStockEvents( String symbol ) {

        return this.stockEventsDataStore.findBySymbol( symbol )
                .map( entity -> {

                    try {

                        return this.mapper.readValue( entity.event(), DomainEvent.class );

                    } catch( JsonProcessingException e ) {
                        log.error( "getStockEvents : error", e );

                        throw Exceptions.propagate( e );
                    }
                });
    }

    @Override
    public Mono<DomainEvent> save( final DomainEvent event ) {

        try {

            UUID id = this.uuidGenerator.generate();
            String json = this.mapper.writeValueAsString( event );

            return this.stockEventsDataStore.save( new DomainEventEntity( id, event.symbol(), event.occurredOn(), json ) )
                    .map( saved -> event );

        } catch( JsonProcessingException e ) {
            log.error( "save : error", e );

            throw Exceptions.propagate( e );
        }

    }

    @Override
    public Mono<StockCache> cacheStock( final StockCache stockCache ) {

        this.stockCacheDataStore.save( new StockCacheEntity( stockCache.symbol(), stockCache.price(), stockCache.lastPriceChanged() ) );

        return Mono.just( stockCache );
    }

    @Override
    public Mono<StockCache> lookupBySymbol( final String symbol ) {

        return this.stockCacheDataStore.findBySymbol( symbol )
                .log()
                .map( entity -> new StockCache( entity.symbol(), entity.price(), entity.lastPriceChanged() ) );
    }

}

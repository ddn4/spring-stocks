package com.vmware.labs.stockService.stock.adapter.out;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmware.labs.stockService.common.persistence.PersistenceAdapter;
import com.vmware.labs.stockService.common.useCase.UuidGenerator;
import com.vmware.labs.stockService.stock.application.out.*;
import com.vmware.labs.stockService.stock.domain.StockCache;
import com.vmware.labs.stockService.stock.domain.events.DomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Slf4j
@PersistenceAdapter
@RequiredArgsConstructor
public class StockPersistenceAdapter implements StockExistsPort, GetStockEventsPort, PersistStockEventPort, PutStockCachePort, LookupStockPort {

    private final StockEventsDataStore stockEventsDataStore;
    private final StockCacheDataStore stockCacheDataStore;
    private final UuidGenerator uuidGenerator;
    private final ObjectMapper mapper;

    @Override
    public boolean exists( final String symbol ) {

        StockCacheEntity entity = this.stockCacheDataStore.findBySymbol( symbol );
        log.debug( "exists : entity = {}", entity );

        return ( null != entity );
    }

    @Override
    public List<DomainEvent> getStockEvents( String symbol ) {

        return this.stockEventsDataStore.findBySymbol( symbol ).stream()
                .map( entity -> {

                    try {

                        return this.mapper.readValue( entity.getEvent(), DomainEvent.class );

                    } catch( JsonProcessingException e ) {
                        log.error( "getStockEvents : error", e );

                        throw new RuntimeException( "event could not be mapped to a DomainEvent", e );
                    }
                })
                .collect( toList() );
    }

    @Override
    public void save( final DomainEvent event ) {

        try {

            UUID id = this.uuidGenerator.generate();
            String json = this.mapper.writeValueAsString( event );

            this.stockEventsDataStore.save( new DomainEventEntity( id, event.symbol(), event.when(), json ) );

        } catch( JsonProcessingException e ) {
            log.error( "save : error", e );

            throw new RuntimeException( "event could not be converted to json", e );
        }

    }

    @Override
    public void cacheStock( final StockCache stockCache ) {

        this.stockCacheDataStore.save( new StockCacheEntity( stockCache.getSymbol(), stockCache.getPrice(), stockCache.getLastPriceChanged() ) );

    }

    @Override
    public StockCache lookupBySymbol( final String symbol ) {

        StockCacheEntity entity = this.stockCacheDataStore.findBySymbol( symbol );
        log.debug( "lookupBySymbol : entity = {}", entity );

        return new StockCache( entity.getSymbol(), entity.getPrice(), entity.getLastPriceChanged() );
    }

}

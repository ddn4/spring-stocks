package com.vmware.labs.stockService.stock.adapter.out;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmware.labs.stockService.common.persistence.PersistenceAdapter;
import com.vmware.labs.stockService.common.useCase.UuidGenerator;
import com.vmware.labs.stockService.stock.application.out.GetStockEventsPort;
import com.vmware.labs.stockService.stock.application.out.PersistStockEventPort;
import com.vmware.labs.stockService.stock.domain.events.DomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Slf4j
@PersistenceAdapter
@RequiredArgsConstructor
public class StockPersistenceAdapter implements GetStockEventsPort, PersistStockEventPort {

    private final StockDataStore dataStore;
    private final UuidGenerator uuidGenerator;
    private final ObjectMapper mapper;

    @Override
    public List<DomainEvent> getStockEvents( String symbol ) {

        return this.dataStore.findBySymbol( symbol ).stream()
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

            this.dataStore.save( new DomainEventEntity( id, event.symbol(), event.when(), json ) );

        } catch (JsonProcessingException e) {
            log.error( "save : error", e );

            throw new RuntimeException( "event could not be converted to json", e );
        }

    }

}

package com.vmware.labs.stockservice.stock.adapter.out.persistence;

import com.vmware.labs.stockservice.common.persistence.PersistenceAdapter;
import com.vmware.labs.stockservice.common.usecase.UuidGenerator;
import com.vmware.labs.stockservice.stock.application.out.*;
import com.vmware.labs.stockservice.stock.domain.StockProjection;
import com.vmware.labs.stockservice.stock.domain.events.DomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@PersistenceAdapter
@RequiredArgsConstructor
public class StockProjectionPersistenceAdapter implements GetStockEventsPort, PersistStockEventPort, UpdateStockProjectionPort, LookupStockProjectionPort, GetAllStockProjectionsPort {

    private final StockDomainEventRepository stockDomainEventRepository;
    private final StockProjectionRepository stockProjectionRepository;
    private final UuidGenerator uuidGenerator;

    @Override
    public Flux<DomainEvent> getStockEvents( String symbol ) {

        return this.stockDomainEventRepository.findAllBySymbol( symbol )
                .map( DomainEventEntity::event );
    }

    @Override
    @Transactional
    public Mono<DomainEvent> save( final DomainEvent event ) {
        log.debug( "save : enter" );

        var id = this.uuidGenerator.generate();

        return this.stockDomainEventRepository.save( new DomainEventEntity( id, null,  event.symbol(), event.occurredOn(), event ) )
                .map( saved -> event );
    }

    @Override
    @Transactional
    public Mono<StockProjection> updateProjection( final StockProjection stockProjection ) {
        log.debug( "updateProjection : enter" );

        return this.stockProjectionRepository.findBySymbol( stockProjection.symbol() )
                .map( entity -> Pair.of( entity.id(), entity.version() ) )
                .switchIfEmpty( Mono.just( Pair.of( this.uuidGenerator.generate(), -1L )  ) )
                .flatMap( pair -> this.stockProjectionRepository.save( new StockProjectionEntity( pair.getFirst(), ( -1 == pair.getSecond() ? null : pair.getSecond() ), stockProjection.symbol(), stockProjection.price(), stockProjection.lastPriceChanged() ) ) )
                .map( entity -> new StockProjection( entity.symbol(), entity.price(), entity.lastPriceChanged() ) );
    }

    @Override
    public Mono<StockProjection> lookupBySymbol( final String symbol ) {

        return this.stockProjectionRepository.findBySymbol( symbol )
                .log()
                .map( entity -> new StockProjection( entity.symbol(), entity.price(), entity.lastPriceChanged() ) );
    }

    @Override
    public Flux<StockProjection> findAll() {

        return this.stockProjectionRepository.findAll()
                .log()
                .map( entity -> new StockProjection( entity.symbol(), entity.price(), entity.lastPriceChanged() ) );
    }

}

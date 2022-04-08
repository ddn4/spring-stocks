package com.vmware.labs.stockservice.stock.application;

import com.vmware.labs.common.annotations.usecase.UseCase;
import com.vmware.labs.components.autoconfigure.TimestampGenerator;
import com.vmware.labs.stockservice.applicationevents.StockUpdatedEvent;
import com.vmware.labs.stockservice.stock.application.in.ChangePriceUseCase;
import com.vmware.labs.stockservice.stock.application.out.GetStockEventsPort;
import com.vmware.labs.stockservice.stock.application.out.PersistStockEventPort;
import com.vmware.labs.stockservice.stock.domain.Stock;
import com.vmware.labs.stockservice.stock.domain.commands.ChangePrice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.util.List.of;

@Slf4j
@UseCase
@RequiredArgsConstructor
public class ChangePriceService implements ChangePriceUseCase {

    private final GetStockEventsPort getStockEventsPort;
    private final PersistStockEventPort persistStockEventPort;
    private final TimestampGenerator timestampGenerator;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public Mono<Stock> execute( final ChangePriceCommand command ) {
        log.debug( "execute : enter" );

        // lookup stock by symbol
        return this.getStockEventsPort.getStockEvents( command.getSymbol() )
                .collectList()
                .map( found -> Stock.createFrom( command.getSymbol(), found ) )
                .switchIfEmpty( Mono.just( Stock.createFrom( command.getSymbol(), of() ) ) )
                .map( stock -> {

                    stock.flushChanges();

                    stock.changePrice( new ChangePrice( command.getSymbol(), command.getPrice(), this.timestampGenerator.generate() ) );
                    Flux.fromIterable( stock.changes() )
                            .flatMap( this.persistStockEventPort::save )
                            .log()
                            .subscribe();
                    stock.flushChanges();
                    log.info( "execute : stock updated [{}]", stock );

                    return stock;
                })
                .doOnNext( stock -> this.applicationEventPublisher.publishEvent( new StockUpdatedEvent( stock.symbol() ) ) );

    }

}

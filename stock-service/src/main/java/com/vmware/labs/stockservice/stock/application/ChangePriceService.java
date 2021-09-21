package com.vmware.labs.stockservice.stock.application;

import com.vmware.labs.stockservice.applicationevents.StockUpdatedEvent;
import com.vmware.labs.stockservice.common.usecase.TimestampGenerator;
import com.vmware.labs.stockservice.common.usecase.UseCase;
import com.vmware.labs.stockservice.stock.application.in.ChangePriceUseCase;
import com.vmware.labs.stockservice.stock.application.out.GetStockEventsPort;
import com.vmware.labs.stockservice.stock.application.out.PersistStockEventPort;
import com.vmware.labs.stockservice.stock.domain.Stock;
import com.vmware.labs.stockservice.stock.domain.commands.ChangePrice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import reactor.core.publisher.Mono;

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
                .doOnNext( Stock::flushChanges )
                .doOnNext( foundStock -> foundStock.changePrice( new ChangePrice( command.getSymbol(), command.getPrice(), this.timestampGenerator.generate() ) ) )
                .doOnNext( foundStock -> foundStock.changes().forEach( this.persistStockEventPort::save ) )
                .doOnNext( Stock::flushChanges )
                .doOnNext( foundStock -> this.applicationEventPublisher.publishEvent( new StockUpdatedEvent( this, foundStock.symbol() ) ) );

    }

}

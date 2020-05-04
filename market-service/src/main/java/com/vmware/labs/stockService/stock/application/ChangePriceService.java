package com.vmware.labs.stockService.stock.application;

import com.vmware.labs.stockService.applicationEvents.StockUpdatedEvent;
import com.vmware.labs.stockService.common.useCase.TimestampGenerator;
import com.vmware.labs.stockService.common.useCase.UseCase;
import com.vmware.labs.stockService.stock.application.in.ChangePriceUseCase;
import com.vmware.labs.stockService.stock.application.out.GetStockEventsPort;
import com.vmware.labs.stockService.stock.application.out.PersistStockEventPort;
import com.vmware.labs.stockService.stock.domain.Stock;
import com.vmware.labs.stockService.stock.domain.commands.ChangePrice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;

@Slf4j
@UseCase
@RequiredArgsConstructor
public class ChangePriceService implements ChangePriceUseCase {

    private final GetStockEventsPort getStockEventsPort;
    private final PersistStockEventPort persistStockEventPort;
    private final TimestampGenerator timestampGenerator;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void execute( final ChangePriceCommand command ) {
        log.debug( "execute : enter" );

        // lookup stock by symbol
        Stock foundStock = Stock.createFrom( command.getSymbol(), this.getStockEventsPort.getStockEvents( command.getSymbol() ) );
        foundStock.flushChanges();  // We don't want to re-record the already persisted events
        log.trace( "execute : found stock [{}]", foundStock.symbol() );

        // record price change
        foundStock.changePrice( new ChangePrice( command.getSymbol(), command.getPrice(), this.timestampGenerator.generate() ) );
        log.trace( "execute : changed price of stock [{}:{}]", foundStock.symbol(), foundStock.currentPrice() );

        // persist event
        foundStock.changes().forEach( this.persistStockEventPort::save );
        foundStock.flushChanges(); // Always flush changes for consistency
        log.trace( "execute : saved dirty events" );

        this.applicationEventPublisher.publishEvent( new StockUpdatedEvent( this, command.getSymbol() ) );
        log.debug( "execute : published application event [StockUpdatedEvent]!" );

        log.debug( "execute : exit" );
    }

}

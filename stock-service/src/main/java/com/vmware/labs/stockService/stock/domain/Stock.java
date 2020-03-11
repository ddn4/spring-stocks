package com.vmware.labs.stockService.stock.domain;

import com.google.common.collect.ImmutableList;
import com.vmware.labs.stockService.stock.domain.commands.ChangePrice;
import com.vmware.labs.stockService.stock.domain.events.DomainEvent;
import com.vmware.labs.stockService.stock.domain.events.PriceChanged;
import io.vavr.API;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.control.Try;
import lombok.With;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.Predicates.instanceOf;

@Slf4j
@With
public class Stock {

    private static final Function2<Stock, PriceChanged, Stock> priceChanged =
        (state, event) -> 
            state
                .withSymbol( event.symbol() )
                .withCurrentPrice( event.price() )
                .withLastPriceChange( event.when() );

    private static final Function2<Stock, DomainEvent, Stock> appendChange =
        (state, event) -> 
            state
                .handleEvent( event )
                    .withChanges( 
                        ImmutableList.<DomainEvent>builder()
                            .addAll( state.changes )
                            .add( event )
                            .build() 
                        );

    private static final Function2<Stock, ChangePrice, DomainEvent> changePrice =
            (state, command) ->
                    new PriceChanged(
                            command.getSymbol(),
                            command.getPrice(),
                            command.getWhen()
                    );

    private final String symbol;
    private final BigDecimal currentPrice;
    private final Instant lastPriceChange;

    private final ImmutableList<DomainEvent> changes;
    
    public Stock( final String symbol ) {

        this( symbol, new BigDecimal("0.00" ), Instant.now(), ImmutableList.of() );

    }

    private Stock( final String symbol, final BigDecimal currentPrice, final Instant lastPriceChange, final ImmutableList<DomainEvent> changes ) {

        this.symbol = symbol;
        this.currentPrice = currentPrice;
        this.lastPriceChange = lastPriceChange;

        this.changes = changes;

    }

    // Attribute Accessors
    public String symbol() {

        return this.symbol;
    }

    public BigDecimal currentPrice() {

        return this.currentPrice;
    }

    public Instant lastPriceChange() {

        return this.lastPriceChange;
    }

    // Actions
    public Try<Stock> changePrice( final ChangePrice command ) {

        return Try.of( () -> {

            throwIfPriceIsNull( command.getPrice() );
            throwIfPriceIsLessThenZero( command.getPrice(), "Price can not be less than zero [price: %s]!");

            return appendChange.apply( this, changePrice.apply( this, command ) );
        })
                .onSuccess( event -> log.info( "changePrice : Stock[{}] price changed successfully [price: {}!", this.symbol, this.currentPrice ) )
                .onFailure( e -> log.error( "changePrice : validation failed", e ) );

    }

    private static final Function1<Stock, Stock> noOp =
            (state) -> state;

    // History
    public List<DomainEvent> changes() {

        return Collections.unmodifiableList( this.changes );
    }

    // Validators
    private void throwIfPriceIsNull( BigDecimal price ) {

        if( null == price ) {

            throw new IllegalArgumentException( "Price must not be null!" );
        }

    }

    private void throwIfPriceIsLessThenZero( BigDecimal price, String msg ) {

        if( price.doubleValue() <= 0.00 ) {

            throw new IllegalArgumentException( String.format( msg, price ) );
        }

    }

    // Rebuild Aggregate
    public static Stock createFrom( final String symbol, List<DomainEvent> history ) {

        return io.vavr.collection.List.ofAll( history )
                .foldLeft( new Stock( symbol ), Stock::handleEvent );
    }

    private Stock handleEvent( final DomainEvent event ) {

        return API.Match( event ).of(
            Case( $( instanceOf( PriceChanged.class ) ), this::priceChanged )
        );
    }

    private Stock priceChanged( final PriceChanged event ) {

        return priceChanged.apply( this, event );
    }

}
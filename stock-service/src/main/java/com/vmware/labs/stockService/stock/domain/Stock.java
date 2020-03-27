package com.vmware.labs.stockService.stock.domain;

import com.vmware.labs.stockService.stock.domain.commands.ChangePrice;
import com.vmware.labs.stockService.stock.domain.events.DomainEvent;
import com.vmware.labs.stockService.stock.domain.events.PriceChanged;
import io.vavr.API;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.Predicates.instanceOf;
import static io.vavr.collection.List.ofAll;

public class Stock {

    private final String symbol;
    private BigDecimal currentPrice;
    private Instant lastPriceChange;

    private List<DomainEvent> changes = new ArrayList<>();
    
    public Stock( final String symbol ) {

        this.symbol = symbol;

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
    public void changePrice( final ChangePrice command ) {

        throwIfNotThisSymbol( command.getSymbol() );
        throwIfPriceIsNull( command.getPrice() );
        throwIfPriceIsLessThenZero( command.getPrice(), "Price can not be less than zero [price: %s]!");

        priceChanged( new PriceChanged( this.symbol, command.getPrice(), command.getWhen() ) );

    }

    private Stock priceChanged( final PriceChanged event ) {

        this.currentPrice = event.price();
        this.lastPriceChange = event.when();

        this.changes.add( event );

        return this;
    }

    private Stock noOp( final PriceChanged event ) {

        this.changes.add( event );

        return this;
    }

    // History
    public List<DomainEvent> changes() {

        return Collections.unmodifiableList( this.changes );
    }

    public void flushChanges() {

        this.changes = new ArrayList<>();

    }

    @Override
    public boolean equals( Object o ) {

        if( this == o ) return true;
        if( o == null || getClass() != o.getClass() ) return false;

        Stock stock = (Stock) o;

        return symbol.equals( stock.symbol );
    }

    @Override
    public int hashCode() {

        return Objects.hash( symbol );
    }

    @Override
    public String toString() {

        return "Stock{" +
                "symbol='" + symbol + '\'' +
                ", currentPrice=" + currentPrice +
                ", lastPriceChange=" + lastPriceChange +
                '}';
    }

    // Validators
    private void throwIfNotThisSymbol( final String value ) {

        if( !this.symbol.equals( value ) ) {

            throw new IllegalStateException( String.format( "Symbol [%s] does not match target symbol [%s]!", this.symbol, value ) );
        }

    }

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

        return ofAll( history )
                .foldLeft( new Stock( symbol ), Stock::handleEvent );
    }

    private Stock handleEvent( final DomainEvent event ) {

        return API.Match( event ).of(
            Case( $( instanceOf( PriceChanged.class ) ), this::priceChanged )
        );
    }

}
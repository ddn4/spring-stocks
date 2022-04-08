package com.vmware.labs.stockservice.stock.domain;

import com.vmware.labs.stockservice.stock.domain.commands.ChangePrice;
import com.vmware.labs.stockservice.stock.domain.events.PriceChanged;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StockTests {

    private String fakeSymbol = "vmw";
    private BigDecimal fakePrice = new BigDecimal( "100.00" );
    private Instant fakeOccurredOn = Instant.now();

    @Test
    void testCreateFrom() {

        var fakePriceChangedEvent = new PriceChanged( fakeSymbol, fakePrice, fakeOccurredOn );

        var actual = Stock.createFrom( fakeSymbol, of( fakePriceChangedEvent ) );

        assertThat( actual ).isNotNull();
        assertThat( actual.symbol() ).isEqualTo( fakeSymbol );
        assertThat( actual.currentPrice() ).isEqualTo( fakePrice );
        assertThat( actual.lastPriceChange() ).isEqualTo( fakeOccurredOn );

    }

    @Test
    void testCreateFrom_verifyIllegalArgumentException_whenPriceIsNull() {

        PriceChanged fakePriceChangedEvent = new PriceChanged( fakeSymbol, fakePrice, fakeOccurredOn );

        var actual = Stock.createFrom( fakeSymbol, of( fakePriceChangedEvent ) );

        var fakeChangePrice = new ChangePrice( fakeSymbol, null, fakeOccurredOn );
        assertThrows( IllegalArgumentException.class, () -> actual.changePrice( fakeChangePrice ) );

    }

    @Test
    void testCreateFrom_verifyIllegalArgumentException_whenPriceIsLessThenZero() {

        var fakePriceChangedEvent = new PriceChanged( fakeSymbol, fakePrice, fakeOccurredOn );

        var actual = Stock.createFrom( fakeSymbol, of( fakePriceChangedEvent ) );

        var fakeChangePrice = new ChangePrice( fakeSymbol, new BigDecimal( "-1.00" ), fakeOccurredOn );
        assertThrows( IllegalArgumentException.class, () -> actual.changePrice( fakeChangePrice ) );

    }

    @Test
    void testCreateFrom_whenMultiplePriceChangedEvents() {

        var today = LocalDateTime.ofInstant( fakeOccurredOn, ZoneOffset.UTC );

        var fakePriceChangedEvent1 = new PriceChanged( fakeSymbol, new BigDecimal( "100.00" ), today.minusDays( 5 ).toInstant( ZoneOffset.UTC ) );
        var fakePriceChangedEvent2 = new PriceChanged( fakeSymbol, new BigDecimal( "101.00" ), today.minusDays( 4 ).toInstant( ZoneOffset.UTC ) );
        var fakePriceChangedEvent3 = new PriceChanged( fakeSymbol, new BigDecimal( "99.00" ), today.minusDays( 3 ).toInstant( ZoneOffset.UTC ) );
        var fakePriceChangedEvent4 = new PriceChanged( fakeSymbol, new BigDecimal( "98.00" ), today.minusDays( 2 ).toInstant( ZoneOffset.UTC ) );
        var fakePriceChangedEvent5 = new PriceChanged( fakeSymbol, new BigDecimal( "110.00" ), today.minusDays( 1 ).toInstant( ZoneOffset.UTC ) );

        var actual = Stock.createFrom( fakeSymbol, of( fakePriceChangedEvent1, fakePriceChangedEvent2, fakePriceChangedEvent3, fakePriceChangedEvent4, fakePriceChangedEvent5 ) );

        assertThat( actual ).isNotNull();
        assertThat( actual.symbol() ).isEqualTo( fakeSymbol );
        assertThat( actual.currentPrice() ).isEqualTo( new BigDecimal( "110.00" ) );
        assertThat( actual.lastPriceChange() ).isEqualTo( today.minusDays( 1 ).toInstant( ZoneOffset.UTC ) );

    }

}

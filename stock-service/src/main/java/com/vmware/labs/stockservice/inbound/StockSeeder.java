package com.vmware.labs.stockservice.inbound;

import com.vmware.labs.stockservice.applicationevents.StockUpdateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty( value = "spring.stocks.seeder.enabled", havingValue = "true" )
class StockSeeder implements ApplicationListener<ApplicationReadyEvent> {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void onApplicationEvent( ApplicationReadyEvent event ) {
        log.info( "onApplicationEvent : received event {}", event );

        this.applicationEventPublisher.publishEvent( new StockUpdateEvent( this, "PVTL", new BigDecimal( "15.00" ) ) );
        this.applicationEventPublisher.publishEvent( new StockUpdateEvent( this, "VMW", new BigDecimal( "121.30" ) ) );
        this.applicationEventPublisher.publishEvent( new StockUpdateEvent( this, "GOOGL", new BigDecimal( "1138.02" ) ) );
        this.applicationEventPublisher.publishEvent( new StockUpdateEvent( this, "AMZN", new BigDecimal( "1965.21" ) ) );
        this.applicationEventPublisher.publishEvent( new StockUpdateEvent( this, "NFLX", new BigDecimal( "374.22" ) ) );
        this.applicationEventPublisher.publishEvent( new StockUpdateEvent( this, "TSLA", new BigDecimal( "512.30" ) ) );

    }

}

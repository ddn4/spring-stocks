package com.vmware.labs.marketService.market.adapter.out.persistence;

import com.vmware.labs.marketService.common.persistence.PersistenceAdapter;
import com.vmware.labs.marketService.market.application.CurrentMarketStatus;
import com.vmware.labs.marketService.market.application.MarketStatus;
import com.vmware.labs.marketService.market.application.out.GetMarketStatusPort;
import com.vmware.labs.marketService.market.application.out.UpdateMarketStatusPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

import static com.vmware.labs.marketService.market.application.MarketStatus.UNKNOWN;
import static java.util.Comparator.comparing;

@Slf4j
@PersistenceAdapter
@RequiredArgsConstructor
public class MarketStatusPersistenceAdapter implements GetMarketStatusPort, UpdateMarketStatusPort {

    private final MarketStatusEventRepository repository;

    @Override
    public CurrentMarketStatus currentStatus() {

        LocalDateTime today = LocalDateTime.now().toLocalDate().atStartOfDay();

        return this.repository.findAllByOccurredAfter( today ).stream()
                .max( comparing( MarketStatusEvent::getOccurred ) )
                .map( event ->
                        new CurrentMarketStatus( MarketStatus.valueOf( event.getStatus() ), event.getOccurred() )
                )
                .orElse(
                        new CurrentMarketStatus( UNKNOWN, LocalDateTime.now() )
                 );
    }

    @Override
    public void setCurrentStatus( final MarketStatus marketStatus, final LocalDateTime occurred ) {

        MarketStatusEvent event = new MarketStatusEvent();
        event.setStatus( marketStatus.name() );
        event.setOccurred( occurred );

        this.repository.save( event );
        log.info( "setCurrentStatus : market status updated {}", event );

    }

}

package com.vmware.labs.marketservice.market.adapter.out.persistence;

import com.vmware.labs.common.annotations.persistence.PersistenceAdapter;
import com.vmware.labs.components.autoconfigure.UuidGenerator;
import com.vmware.labs.marketservice.market.application.CurrentMarketStatus;
import com.vmware.labs.marketservice.market.application.MarketStatus;
import com.vmware.labs.marketservice.market.application.MarketStatusState;
import com.vmware.labs.marketservice.market.application.out.GetMarketStatusPort;
import com.vmware.labs.marketservice.market.application.out.UpdateMarketStatusPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@PersistenceAdapter
@RequiredArgsConstructor
public class MarketStatusPersistenceAdapter implements GetMarketStatusPort, UpdateMarketStatusPort {

    private final MarketStatusEventRepository repository;
    private final UuidGenerator uuidGenerator;

    @Override
    public Mono<CurrentMarketStatus> currentStatus() {

        LocalDateTime today = LocalDateTime.now().toLocalDate().atStartOfDay();

        return this.repository.findTop1ByOccurredAfterOrderByOccurredDesc( today )
                .map( event -> new CurrentMarketStatus( MarketStatus.valueOf( event.status() ), event.occurred() ) );
    }

    @Override
    @Transactional
    public Mono<MarketStatusState> setCurrentStatus( final MarketStatus marketStatus, final LocalDateTime occurred ) {
        log.debug( "setCurrentStatus : enter" );

        var event = new MarketStatusEvent( this.uuidGenerator.generate(), null, marketStatus.name(), occurred );

        return this.repository.save( event )
                .log()
                .map( saved ->
                        new MarketStatusState(
                                saved.id(),
                                MarketStatus.valueOf( saved.status() ),
                                saved.occurred()
                        )
                );

    }

}

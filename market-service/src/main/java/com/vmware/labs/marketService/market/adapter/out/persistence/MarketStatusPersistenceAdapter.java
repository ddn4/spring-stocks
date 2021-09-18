package com.vmware.labs.marketService.market.adapter.out.persistence;

import com.vmware.labs.marketService.common.persistence.PersistenceAdapter;
import com.vmware.labs.marketService.common.useCase.UuidGenerator;
import com.vmware.labs.marketService.market.application.CurrentMarketStatus;
import com.vmware.labs.marketService.market.application.MarketStatus;
import com.vmware.labs.marketService.market.application.MarketStatusState;
import com.vmware.labs.marketService.market.application.out.GetMarketStatusPort;
import com.vmware.labs.marketService.market.application.out.UpdateMarketStatusPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

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
                .map( event -> new CurrentMarketStatus( MarketStatus.valueOf( event.getStatus() ), event.getOccurred() ) );
    }

    @Override
    @Transactional
    public Mono<MarketStatusState> setCurrentStatus( final MarketStatus marketStatus, final LocalDateTime occurred ) {
        log.debug( "setCurrentStatus : enter" );

        var event = new MarketStatusEvent();
        event.setId( this.uuidGenerator.generate().toString() );
        event.setStatus( marketStatus.name() );
        event.setOccurred( occurred );

        return this.repository.save( event )
                .log()
                .map( saved ->
                        new MarketStatusState(
                                UUID.fromString( saved.getId() ),
                                MarketStatus.valueOf( saved.getStatus() ),
                                saved.getOccurred()
                        )
                );

    }

}

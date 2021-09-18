package com.vmware.labs.marketService.scheduledTasks;

import com.vmware.labs.marketService.market.application.in.CloseMarketUseCase;
import com.vmware.labs.marketService.market.application.in.CloseMarketUseCase.CloseMarketCommand;
import com.vmware.labs.marketService.market.application.in.OpenMarketUseCase;
import com.vmware.labs.marketService.market.application.in.OpenMarketUseCase.OpenMarketCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.support.leader.LockRegistryLeaderInitiator;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class ScheduleConfig {

    private final OpenMarketUseCase openMarketUseCase;
    private final CloseMarketUseCase closeMarketUseCase;
    private final LockRegistryLeaderInitiator leaderInitiator;

    @Scheduled( cron = "${market-service.open-cron}" )
    public void openMarketTask() {
        log.info( "openMarketTask : enter" );

        if( this.leaderInitiator.getContext().isLeader() ) {
            log.info( "openMarketTask : leader is executing" );

            this.openMarketUseCase.execute( new OpenMarketCommand() )
                    .log()
                    .subscribe();

        }

        log.info( "openMarketTask : exit" );
    }

    @Scheduled( cron = "${market-service.close-cron}" )
    public void closeMarketTask() {
        log.info( "closeMarketTask : enter" );

        if( this.leaderInitiator.getContext().isLeader() ) {
            log.info( "closeMarketTask : leader is executing" );

            this.closeMarketUseCase.execute( new CloseMarketCommand() )
                    .log()
                    .subscribe();

        }

        log.info( "closeMarketTask : exit" );
    }

}

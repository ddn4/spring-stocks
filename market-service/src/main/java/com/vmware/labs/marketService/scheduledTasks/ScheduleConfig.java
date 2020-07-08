package com.vmware.labs.marketService.scheduledTasks;

import com.vmware.labs.marketService.market.application.in.CloseMarketUseCase;
import com.vmware.labs.marketService.market.application.in.CloseMarketUseCase.CloseMarketCommand;
import com.vmware.labs.marketService.market.application.in.OpenMarketUseCase;
import com.vmware.labs.marketService.market.application.in.OpenMarketUseCase.OpenMarketCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.transaction.Transactional;

@Slf4j
@Configuration
@EnableScheduling
@EnableSchedulerLock( defaultLockAtMostFor = "30s" )
@RequiredArgsConstructor
public class ScheduleConfig {

    private final OpenMarketUseCase openMarketUseCase;
    private final CloseMarketUseCase closeMarketUseCase;

    /* Cron Format
     *
     * second, minute, hour, day of month, month, day(s) of week
     *
     */

    @Scheduled( cron = "0 0 9 * * MON-FRI" )
//    @Scheduled( cron = "0 */2 * * * MON-FRI" )
    @SchedulerLock( name = "open market task", lockAtMostFor = "1m", lockAtLeastFor = "1m" )
    @Transactional
    public void openMarketTask() throws InterruptedException {
        log.info( "openMarketTask : enter" );

        OpenMarketCommand command = new OpenMarketCommand();
        this.openMarketUseCase.execute( command );
        log.info( "openMarketTask : market opened at {}", command.getTimeOpened() );

        log.info( "openMarketTask : exit" );
    }

    @Scheduled( cron = "0 30 4 * * MON-FRI" )
    @SchedulerLock( name = "close market task", lockAtMostFor = "1m", lockAtLeastFor = "1m" )
    @Transactional
    public void closeMarketTask() throws InterruptedException {
        log.info( "closeMarketTask : enter" );

        CloseMarketCommand command = new CloseMarketCommand();
        this.closeMarketUseCase.execute( command );
        log.info( "closeMarketTask : market closed at {}", command.getTimeClosed() );

        log.info( "closeMarketTask : exit" );
    }

}

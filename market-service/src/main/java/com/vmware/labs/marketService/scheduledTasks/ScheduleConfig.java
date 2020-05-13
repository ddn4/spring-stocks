package com.vmware.labs.marketService.scheduledTasks;

import com.vmware.labs.marketService.market.application.in.OpenMarketUseCase;
import com.vmware.labs.marketService.market.application.in.OpenMarketUseCase.OpenMarketCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.concurrent.locks.Lock;

import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class ScheduleConfig {

    private final OpenMarketUseCase openMarketUseCase;
    private final LockRegistry registry;

    /* Cron Format
     *
     * second, minute, hour, day of month, month, day(s) of week
     *
     */

    @Scheduled( cron = "0 0 9 * * MON-FRI" )
    public void openMarket() throws InterruptedException {

        var command = new OpenMarketCommand();
        Lock lock = this.registry.obtain( command );
        boolean acquired = lock.tryLock( 1, SECONDS );
        if( acquired ) {

            try {

                this.openMarketUseCase.execute( command );
                log.info( "openMarket : market opened at {}", LocalDateTime.now());

            } catch( Exception e ) {
                log.error( "openMarket : error, scheduled task failed!", e );

            } finally {

                lock.unlock();

            }

        }

    }

}

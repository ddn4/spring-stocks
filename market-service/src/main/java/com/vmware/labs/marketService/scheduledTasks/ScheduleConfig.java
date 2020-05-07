package com.vmware.labs.marketService.scheduledTasks;

import com.vmware.labs.marketService.market.application.in.OpenMarketUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class ScheduleConfig {

    private final OpenMarketUseCase openMarketUseCase;

    /* Cron Format
     *
     * second, minute, hour, day of month, month, day(s) of week
     *
     */

    @Scheduled( cron = "0 0 9 * * MON-FRI" )
    public void openMarket() {

        this.openMarketUseCase.execute( new OpenMarketUseCase.OpenMarketCommand() );
        log.info( "openMarket : market opened at {}", LocalDateTime.now() );

    }

}

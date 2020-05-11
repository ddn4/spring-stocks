package com.vmware.labs.stockService.inbound;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.function.Consumer;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MarketStatusListenerConfig {

    @Bean
    public Consumer<MarketStatus> marketStatusListener() {

        return event -> log.info( "marketStatusListener : received market status update {}", event );
    }

    @JsonIgnoreProperties( ignoreUnknown = true )
    @ToString
    static class MarketStatus {

        private final String status;
        private final LocalDateTime occurred;

        @JsonCreator
        MarketStatus(
                @JsonProperty( "symbol" ) final String status,
                @JsonProperty( "price" ) final LocalDateTime occurred
        ) {

            this.status = status;
            this.occurred = occurred;

        }

        String getStatus() {

            return this.status;
        }

        LocalDateTime getOccurred() {

            return this.occurred;
        }

    }

}

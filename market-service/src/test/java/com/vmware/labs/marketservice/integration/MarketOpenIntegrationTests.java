package com.vmware.labs.marketservice.integration;

import com.vmware.labs.marketservice.MarketServiceApplication;
import com.vmware.labs.marketservice.common.usecase.UuidGenerator;
import com.vmware.labs.marketservice.market.application.MarketStatusState;
import com.vmware.labs.marketservice.market.application.in.GetMarketStatusQuery;
import com.vmware.labs.marketservice.market.application.in.GetMarketStatusQuery.GetMarketStatusCommand;
import com.vmware.labs.marketservice.market.application.in.OpenMarketUseCase;
import com.vmware.labs.marketservice.market.application.in.OpenMarketUseCase.OpenMarketCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.vmware.labs.marketservice.market.application.MarketStatus.OPEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag( "integration" )
class MarketOpenIntegrationTests {

    @Test
    @DisplayName( "Test Open Market and Get Status (Integration)" )
    void testOpenMarketAndGetCurrentState() {

        try( ConfigurableApplicationContext context =
                     new SpringApplicationBuilder( TestChannelBinderConfiguration.getCompleteConfiguration( MarketServiceApplication.class, TestMarketIntegrationConfiguration.class ) )
                             .web( WebApplicationType.NONE )
                             .profiles( "market-open-integration-test" )
                             .run()
        ) {

            var openMarketUseCase = context.getBean( OpenMarketUseCase.class );
            var getMarketStatusQuery = context.getBean( GetMarketStatusQuery.class );
            var randomUuid = context.getBean( UUID.class );

            var now = LocalDateTime.now();

            var expectedStatus = new MarketStatusState( randomUuid, OPEN, now );

            StepVerifier.create( openMarketUseCase.execute( new OpenMarketCommand( now ) ) )
                    .expectNext( expectedStatus )
                    .expectComplete()
                    .verify();

            StepVerifier.create( getMarketStatusQuery.execute( new GetMarketStatusCommand() ) )
                    .consumeNextWith( map ->
                        assertThat( map ).contains( entry( "marketStatus", OPEN.name() ), entry( "occurred", now.toString() ) )
                    )
                    .expectComplete()
                    .verify();

        }

    }

    @TestConfiguration
    @Profile( "market-open-integration-test" )
    static class TestMarketIntegrationConfiguration {

        UUID randomUuid = UUID.randomUUID();

        @Bean
        @Primary
        UuidGenerator mockUuidGenerator() {

            UuidGenerator mockUuidGenerator = mock( UuidGenerator.class );
            when( mockUuidGenerator.generate() )
                    .thenReturn( randomUuid );

            return mockUuidGenerator;
        }

        @Bean
        UUID randomUuid() {

            return randomUuid;
        }

    }

}


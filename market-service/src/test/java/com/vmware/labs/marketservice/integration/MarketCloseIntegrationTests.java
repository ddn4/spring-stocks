package com.vmware.labs.marketservice.integration;

import com.vmware.labs.marketservice.MarketServiceApplication;
import com.vmware.labs.marketservice.common.usecase.UuidGenerator;
import com.vmware.labs.marketservice.market.application.MarketStatusState;
import com.vmware.labs.marketservice.market.application.in.CloseMarketUseCase;
import com.vmware.labs.marketservice.market.application.in.CloseMarketUseCase.CloseMarketCommand;
import com.vmware.labs.marketservice.market.application.in.GetMarketStatusQuery;
import com.vmware.labs.marketservice.market.application.in.GetMarketStatusQuery.GetMarketStatusCommand;
import org.junit.jupiter.api.*;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.vmware.labs.marketservice.market.application.MarketStatus.CLOSED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Testcontainers
@Tag( "integration" )
class MarketCloseIntegrationTests {

    @Container
    static PostgreSQLContainer<?> postgreDBContainer = new PostgreSQLContainer<>( "postgres" );

    private Map<String, Object> properties = new HashMap<>();

    @BeforeEach
    void initialize() {

        properties.put( "spring.r2dbc.url", "r2dbc:tc:postgresql:///test?TC_IMAGE_TAG=" + PostgreSQLContainer.DEFAULT_TAG );
        properties.put( "spring.r2dbc.username", "test" );
        properties.put( "spring.r2dbc.password", "test" );

        properties.put( "spring.liquibase.url", postgreDBContainer.getJdbcUrl() );
        properties.put( "spring.liquibase.user", "test" );
        properties.put( "spring.liquibase.password", "test" );

//        properties.put( "market-service.open-cron", "0 0 9 * * MON-FRI" );
//        properties.put( "market-service.close-cron", "0 30 4 * * MON-FRI" );

    }

    @AfterEach
    void cleanup() {

        properties = new HashMap<>();

    }

    @Test
    @DisplayName( "Test Close Market and Get Status (Integration)" )
    void testCloseMarketAndGetCurrentState() {

        await().untilAsserted(() -> {
            assertThat( postgreDBContainer.isRunning() ).isTrue();
        });

        try( ConfigurableApplicationContext context =
                     new SpringApplicationBuilder( TestChannelBinderConfiguration.getCompleteConfiguration( MarketServiceApplication.class, TestMarketIntegrationConfiguration.class ) )
                             .web( WebApplicationType.NONE )
                             .properties( properties )
                             .profiles( "market-close-integration-test" )
                             .run()
        ) {

            var closeMarketUseCase = context.getBean( CloseMarketUseCase.class );
            var getMarketStatusQuery = context.getBean( GetMarketStatusQuery.class );
            var randomUuid = context.getBean( UUID.class );

            var now = LocalDateTime.now();

            var expectedStatus = new MarketStatusState( randomUuid, CLOSED, now );

            StepVerifier.create( closeMarketUseCase.execute( new CloseMarketCommand( now ) ) )
                    .expectNext( expectedStatus )
                    .expectComplete()
                    .verify();

            StepVerifier.create( getMarketStatusQuery.execute( new GetMarketStatusCommand() ) )
                    .consumeNextWith( map ->
                            assertThat( map ).contains( entry( "marketStatus", CLOSED.name() ), entry( "occurred", now.toString() ) )
                    )
                    .expectComplete()
                    .verify();

        }

    }

    @TestConfiguration
    @Profile( "market-close-integration-test" )
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


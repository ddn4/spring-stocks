package com.vmware.labs.components.autoconfigure;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.autoconfigure.AutoConfigurations.of;

public class TimestampGeneratorAutoConfigurationTests {

    private final ApplicationContextRunner contextRunner =
            new ApplicationContextRunner()
                    .withConfiguration(
                            of(
                                    TimestampGeneratorAutoConfiguration.class
                            )
                    );

    @Test
    void givenAutoConfigurationShouldCreateTimestampGenerator() {
    
        this.contextRunner
                .run( (context) -> assertThat( context ).hasSingleBean( TimestampGenerator.class ));
        
    }
    
}

package com.vmware.labs.components.autoconfigure;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.autoconfigure.AutoConfigurations.of;

public class UuidGeneratorAutoConfigurationTests {

    private final ApplicationContextRunner contextRunner =
            new ApplicationContextRunner()
                    .withConfiguration(
                            of(
                                    UuidGeneratorAutoConfiguration.class
                            )
                    );

    @Test
    void givenAutoConfigurationShouldCreateUuidGenerator() {
    
        this.contextRunner
                .run( (context) -> assertThat( context ).hasSingleBean( UuidGenerator.class ));
        
    }
    
}

package com.vmware.labs.components.autoconfigure;

import org.springframework.context.annotation.Import;

@Import({
        TimestampGeneratorConfigurations.class
})
public class TimestampGeneratorAutoConfiguration {
}

package com.vmware.labs.components.autoconfigure;

import org.springframework.context.annotation.Import;

@Import({
        UuidGeneratorConfigurations.class
})
public class UuidGeneratorAutoConfiguration {
}

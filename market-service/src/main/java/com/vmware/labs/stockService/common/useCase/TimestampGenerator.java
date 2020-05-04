package com.vmware.labs.stockService.common.useCase;

import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class TimestampGenerator {

    public Instant generate() {

        return Instant.now();
    }

}

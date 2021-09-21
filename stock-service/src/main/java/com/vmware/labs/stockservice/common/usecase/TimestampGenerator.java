package com.vmware.labs.stockservice.common.usecase;

import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class TimestampGenerator {

    public Instant generate() {

        return Instant.now();
    }

}

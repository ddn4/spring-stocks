package com.vmware.labs.components.autoconfigure;

import java.time.Clock;
import java.time.Instant;

public class TimestampGenerator {

    private final Clock clock;
    
    TimestampGenerator( final Clock clock ) {
        
        this.clock = clock;
        
    }
    
    public Instant generate() {

        return Instant.now( this.clock );
    }

}

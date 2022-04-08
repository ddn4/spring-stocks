package com.vmware.labs.stockservice.applicationevents;

import java.time.LocalDateTime;

public record MarketStatusUpdatedEvent( String marketStatus, LocalDateTime occurred ) { }

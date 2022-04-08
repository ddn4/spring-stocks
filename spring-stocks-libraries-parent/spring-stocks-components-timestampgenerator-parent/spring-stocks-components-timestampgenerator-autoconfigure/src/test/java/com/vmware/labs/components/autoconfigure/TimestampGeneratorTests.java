package com.vmware.labs.components.autoconfigure;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.Clock;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TimestampGeneratorTests {

    private static final long FAKE_TIMESTAMP = 228040680;

    private TimestampGenerator subject;

    private MockedStatic<Clock> mockClock;
    
    @BeforeEach
    void setup() {
        
        Clock spyClock = spy( Clock.class );
        mockClock = mockStatic( Clock.class );
        mockClock.when( Clock::systemUTC ).thenReturn( spyClock );
        when( spyClock.instant() ).thenReturn( Instant.ofEpochSecond( FAKE_TIMESTAMP ) );

        this.subject = new TimestampGenerator( spyClock );

    }
    
    @AfterEach
    void teardown() {
        
        mockClock.close();
        
    }
    
    @Test
    void testGenerate() {

        Instant actual = this.subject.generate();

        assertThat( actual )
                .isNotNull()
                .isEqualTo( Instant.ofEpochSecond( FAKE_TIMESTAMP ) );

    }
    
}

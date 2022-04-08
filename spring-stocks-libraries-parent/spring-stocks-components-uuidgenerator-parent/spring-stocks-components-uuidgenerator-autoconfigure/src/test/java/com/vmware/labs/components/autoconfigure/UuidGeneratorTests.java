package com.vmware.labs.components.autoconfigure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;

class UuidGeneratorTests {

    private static final UUID FAKE_UUID = UUID.fromString( "03000cfc-876e-4c8c-9890-26175c3a3946" );

    private UuidGenerator subject;

    @BeforeEach
    void setup() {
        
        this.subject = new UuidGenerator();

    }
    
    @Test
    void testGenerate() {

        try( MockedStatic<UUID> mockedStatic = mockStatic( UUID.class ) ) {
            
            mockedStatic.when( UUID::randomUUID ).thenReturn( FAKE_UUID );

            UUID actual = this.subject.generate();

            assertThat( actual )
                    .isNotNull()
                    .isEqualTo( FAKE_UUID );

        }
        
    }
    
}

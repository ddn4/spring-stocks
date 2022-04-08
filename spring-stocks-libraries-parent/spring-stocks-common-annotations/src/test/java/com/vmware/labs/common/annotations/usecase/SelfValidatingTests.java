package com.vmware.labs.common.annotations.usecase;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class SelfValidatingTests {

    @Test
    void testConstraintViolation() {
    
        assertThrows( ConstraintViolationException.class, () -> new Subject( null ) );
    }
    
}

class Subject extends SelfValidating<Subject> {
    
    @NotNull
    UUID id;
    
    Subject( final UUID id ) {
        
        this.id = id;
        
        validateSelf();
        
    }
    
}

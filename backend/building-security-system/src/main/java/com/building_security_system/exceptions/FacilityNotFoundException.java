package com.building_security_system.exceptions;

import java.io.Serial;

public class FacilityNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1;

    public FacilityNotFoundException(String message) {
        super(message);
    }
}
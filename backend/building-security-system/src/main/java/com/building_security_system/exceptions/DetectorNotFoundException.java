package com.building_security_system.exceptions;

import java.io.Serial;

public class DetectorNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1;

    public DetectorNotFoundException(String message) {
        super(message);
    }
}

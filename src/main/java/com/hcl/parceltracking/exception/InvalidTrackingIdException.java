package com.hcl.parceltracking.exception;

/**
 * Exception thrown when tracking ID format is invalid.
 */
public class InvalidTrackingIdException extends RuntimeException {
    
    public InvalidTrackingIdException(String message) {
        super(message);
    }
    
    public InvalidTrackingIdException(String message, Throwable cause) {
        super(message, cause);
    }
}

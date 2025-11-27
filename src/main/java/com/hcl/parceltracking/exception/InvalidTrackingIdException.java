package com.hcl.parceltracking.exception;

/**
 * Exception thrown when a tracking ID format is invalid.
 * 
 * @author HCL Technologies
 * @version 1.0.0
 */
public class InvalidTrackingIdException extends RuntimeException {

    /**
     * Constructor with message.
     * 
     * @param message the error message
     */
    public InvalidTrackingIdException(String message) {
        super(message);
    }

    /**
     * Constructor with message and cause.
     * 
     * @param message the error message
     * @param cause the cause of the exception
     */
    public InvalidTrackingIdException(String message, Throwable cause) {
        super(message, cause);
    }
}

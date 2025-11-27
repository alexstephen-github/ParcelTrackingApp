package com.hcl.parceltracking.exception;

/**
 * Exception thrown when a requested resource is not found.
 * 
 * @author HCL Technologies
 * @version 1.0.0
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructor with message.
     * 
     * @param message the error message
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor with message and cause.
     * 
     * @param message the error message
     * @param cause the cause of the exception
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

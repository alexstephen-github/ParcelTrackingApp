package com.hcl.parceltracking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for standardized error responses.
 * 
 * This DTO is returned whenever an error occurs in the API.
 * 
 * @author HCL Technologies
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    
    /**
     * Timestamp when the error occurred
     */
    private LocalDateTime timestamp;
    
    /**
     * HTTP status code
     */
    private int status;
    
    /**
     * Error type or category
     */
    private String error;
    
    /**
     * Detailed error message
     */
    private String message;
    
    /**
     * Request path that caused the error
     */
    private String path;
    
    /**
     * Constructor that automatically sets the timestamp
     */
    public ErrorResponse(int status, String error, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }
}

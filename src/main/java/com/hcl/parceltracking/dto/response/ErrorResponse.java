package com.hcl.parceltracking.dto.response;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Standardized error response DTO.
 * Provides consistent error information across all API endpoints.
 */
public record ErrorResponse(
    LocalDateTime timestamp,
    int status,
    String error,
    String message,
    String path,
    List<String> details
) {
    public ErrorResponse(LocalDateTime timestamp, int status, String error, String message, String path) {
        this(timestamp, status, error, message, path, Collections.emptyList());
    }
}

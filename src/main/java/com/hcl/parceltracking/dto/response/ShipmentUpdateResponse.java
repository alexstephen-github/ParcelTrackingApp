package com.hcl.parceltracking.dto.response;

import java.time.LocalDateTime;

/**
 * Response DTO for shipment update operations.
 */
public record ShipmentUpdateResponse(
    String trackingId,
    String message,
    LocalDateTime updatedAt
) {}

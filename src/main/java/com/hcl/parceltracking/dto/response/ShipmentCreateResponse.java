package com.hcl.parceltracking.dto.response;

import java.time.LocalDateTime;

/**
 * Response DTO for shipment creation.
 */
public record ShipmentCreateResponse(
    String trackingId,
    String message,
    LocalDateTime createdAt
) {}

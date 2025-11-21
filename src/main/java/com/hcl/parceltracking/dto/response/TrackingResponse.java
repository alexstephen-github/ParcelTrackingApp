package com.hcl.parceltracking.dto.response;

import com.hcl.parceltracking.model.ShipmentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO for tracking information.
 * Contains all relevant information about a parcel's current status and history.
 */
public record TrackingResponse(
    String trackingId,
    String currentLocation,
    ShipmentStatus status,
    LocalDateTime lastUpdate,
    LocalDate estimatedDelivery,
    List<StatusHistoryDto> history
) {}

package com.hcl.parceltracking.dto.response;

import com.hcl.parceltracking.model.ShipmentStatus;

import java.time.LocalDateTime;

/**
 * DTO for status history information in tracking response.
 */
public record StatusHistoryDto(
    ShipmentStatus status,
    String location,
    LocalDateTime timestamp
) {}

package com.hcl.parceltracking.dto.request;

import com.hcl.parceltracking.model.ShipmentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for updating shipment status.
 */
public record UpdateShipmentRequest(
    @NotNull(message = "Status is required")
    ShipmentStatus status,
    
    @NotBlank(message = "Location is required")
    @Size(max = 255, message = "Location must not exceed 255 characters")
    String location,
    
    @Size(max = 500, message = "Notes must not exceed 500 characters")
    String notes
) {}

package com.hcl.parceltracking.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for creating a new parcel shipment.
 * 
 * This request is used by administrators to register a new parcel in the system.
 * 
 * @author HCL Technologies
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParcelCreateRequest {
    
    /**
     * Origin location where the parcel starts its journey
     */
    @NotBlank(message = "Origin location is required")
    @Size(max = 255, message = "Origin location must not exceed 255 characters")
    private String originLocation;
    
    /**
     * Destination location where the parcel will be delivered
     */
    @NotBlank(message = "Destination location is required")
    @Size(max = 255, message = "Destination location must not exceed 255 characters")
    private String destinationLocation;
    
    /**
     * Name of the recipient
     */
    @NotBlank(message = "Recipient name is required")
    @Size(max = 255, message = "Recipient name must not exceed 255 characters")
    private String recipientName;
    
    /**
     * Full address of the recipient
     */
    @NotBlank(message = "Recipient address is required")
    @Size(max = 1000, message = "Recipient address must not exceed 1000 characters")
    private String recipientAddress;
    
    /**
     * Estimated delivery date
     */
    @Future(message = "Estimated delivery date must be in the future")
    private LocalDate estimatedDeliveryDate;
}

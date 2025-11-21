package com.hcl.parceltracking.dto.request;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

/**
 * Request DTO for creating a new shipment.
 */
public record CreateShipmentRequest(
    @NotBlank(message = "Origin is required")
    @Size(max = 255, message = "Origin must not exceed 255 characters")
    String origin,
    
    @NotBlank(message = "Destination is required")
    @Size(max = 255, message = "Destination must not exceed 255 characters")
    String destination,
    
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    String customerEmail,
    
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    String customerPhone,
    
    @Future(message = "Estimated delivery must be a future date")
    LocalDate estimatedDelivery
) {}

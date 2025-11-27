package com.hcl.parceltracking.dto;

import com.hcl.parceltracking.model.ParcelStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating parcel status.
 * 
 * This request is used by administrators to update the status and location of a parcel.
 * 
 * @author HCL Technologies
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParcelStatusUpdateRequest {
    
    /**
     * New status for the parcel
     */
    @NotNull(message = "Status is required")
    private ParcelStatus status;
    
    /**
     * New location of the parcel
     */
    @NotBlank(message = "Location is required")
    @Size(max = 255, message = "Location must not exceed 255 characters")
    private String location;
    
    /**
     * Optional notes about the status change
     */
    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;
}

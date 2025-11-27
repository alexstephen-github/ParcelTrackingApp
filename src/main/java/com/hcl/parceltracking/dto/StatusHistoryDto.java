package com.hcl.parceltracking.dto;

import com.hcl.parceltracking.model.ParcelStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO representing a single status history entry in the response.
 * 
 * @author HCL Technologies
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusHistoryDto {
    
    /**
     * Status at this point in time
     */
    private ParcelStatus status;
    
    /**
     * Location where the status change occurred
     */
    private String location;
    
    /**
     * Timestamp of the status change
     */
    private LocalDateTime timestamp;
    
    /**
     * Optional notes about the status change
     */
    private String notes;
}

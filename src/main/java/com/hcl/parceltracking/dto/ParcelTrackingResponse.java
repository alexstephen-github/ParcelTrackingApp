package com.hcl.parceltracking.dto;

import com.hcl.parceltracking.model.ParcelStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for parcel tracking response.
 * 
 * This DTO contains all information returned when a customer tracks a parcel.
 * 
 * @author HCL Technologies
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParcelTrackingResponse {
    
    /**
     * Unique tracking identifier
     */
    private String trackingId;
    
    /**
     * Current status of the parcel
     */
    private ParcelStatus currentStatus;
    
    /**
     * Current location of the parcel
     */
    private String currentLocation;
    
    /**
     * Timestamp of the last update
     */
    private LocalDateTime lastUpdateTimestamp;
    
    /**
     * Estimated delivery date
     */
    private LocalDate estimatedDeliveryDate;
    
    /**
     * Complete status history of the parcel
     */
    private List<StatusHistoryDto> statusHistory;
}

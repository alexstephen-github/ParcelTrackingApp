package com.hcl.parceltracking.dto;

import com.hcl.parceltracking.model.ParcelStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for parcel creation response.
 * 
 * This response is returned after successfully creating a new parcel.
 * 
 * @author HCL Technologies
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParcelCreateResponse {
    
    /**
     * Generated tracking identifier
     */
    private String trackingId;
    
    /**
     * Generated barcode (Base64 encoded image)
     */
    private String barcode;
    
    /**
     * Initial status of the parcel
     */
    private ParcelStatus currentStatus;
    
    /**
     * Success message
     */
    private String message;
    
    /**
     * Timestamp when the parcel was created
     */
    private LocalDateTime createdAt;
}

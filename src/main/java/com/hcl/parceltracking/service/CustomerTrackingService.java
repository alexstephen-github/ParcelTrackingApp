package com.hcl.parceltracking.service;

import com.hcl.parceltracking.dto.response.TrackingResponse;

/**
 * Service interface for customer tracking operations.
 */
public interface CustomerTrackingService {
    
    /**
     * Get tracking information for a parcel by tracking ID.
     * 
     * @param trackingId the tracking ID
     * @return tracking response with current status and history
     */
    TrackingResponse getTrackingInfo(String trackingId);
}

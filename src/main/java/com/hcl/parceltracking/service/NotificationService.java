package com.hcl.parceltracking.service;

import com.hcl.parceltracking.model.ParcelStatus;

/**
 * Service interface for notification operations.
 * 
 * @author HCL Technologies
 * @version 1.0.0
 */
public interface NotificationService {

    /**
     * Send notification when parcel status changes.
     * 
     * @param trackingId the tracking identifier
     * @param status the new status
     * @param location the current location
     */
    void sendStatusChangeNotification(String trackingId, ParcelStatus status, String location);
}

package com.hcl.parceltracking.service;

import com.hcl.parceltracking.model.Shipment;
import com.hcl.parceltracking.model.ShipmentStatus;

/**
 * Service interface for notification operations.
 */
public interface NotificationService {
    
    /**
     * Send status update notification to customer via email/SMS.
     * 
     * @param shipment the shipment
     * @param newStatus the new status
     */
    void sendStatusUpdateNotification(Shipment shipment, ShipmentStatus newStatus);
}

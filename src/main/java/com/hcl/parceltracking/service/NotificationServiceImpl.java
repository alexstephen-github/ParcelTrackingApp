package com.hcl.parceltracking.service;

import com.hcl.parceltracking.model.ParcelStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Implementation of NotificationService for sending notifications.
 * 
 * This is a placeholder implementation that logs notifications.
 * In production, this would integrate with email/SMS services.
 * 
 * @author HCL Technologies
 * @version 1.0.0
 */
@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    /**
     * Send notification when parcel status changes.
     * 
     * Currently logs the notification. In production, this would send
     * actual email/SMS notifications based on customer preferences.
     * 
     * @param trackingId the tracking identifier
     * @param status the new status
     * @param location the current location
     */
    @Override
    public void sendStatusChangeNotification(String trackingId, ParcelStatus status, String location) {
        // Key status changes that trigger notifications
        if (status == ParcelStatus.BOOKED || 
            status == ParcelStatus.OUT_FOR_DELIVERY || 
            status == ParcelStatus.DELIVERED) {
            
            logger.info("NOTIFICATION: Parcel {} status changed to {} at {}", 
                       trackingId, status, location);
            
            // TODO: Integrate with email/SMS service
            // Example: emailService.sendStatusNotification(trackingId, status, location);
        }
    }
}

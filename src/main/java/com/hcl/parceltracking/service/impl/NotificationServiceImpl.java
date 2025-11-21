package com.hcl.parceltracking.service.impl;

import com.hcl.parceltracking.model.Shipment;
import com.hcl.parceltracking.model.ShipmentStatus;
import com.hcl.parceltracking.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Implementation of NotificationService.
 * Handles email and SMS notifications for shipment status updates.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    
    private final JavaMailSender mailSender;
    
    @Override
    @Async
    public void sendStatusUpdateNotification(Shipment shipment, ShipmentStatus newStatus) {
        log.info("Sending notification for tracking ID: {} with status: {}", 
                shipment.getTrackingId(), newStatus);
        
        // Send email notification
        if (shipment.getCustomerEmail() != null && !shipment.getCustomerEmail().isBlank()) {
            sendEmailNotification(shipment.getCustomerEmail(), shipment.getTrackingId(), newStatus);
        }
        
        // Send SMS notification if configured
        if (shipment.getCustomerPhone() != null && !shipment.getCustomerPhone().isBlank()) {
            sendSmsNotification(shipment.getCustomerPhone(), shipment.getTrackingId(), newStatus);
        }
    }
    
    /**
     * Send email notification to customer.
     */
    private void sendEmailNotification(String email, String trackingId, ShipmentStatus status) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Shipment Status Update - " + trackingId);
            message.setText(buildEmailBody(trackingId, status));
            
            mailSender.send(message);
            log.info("Email notification sent successfully to: {}", email);
        } catch (Exception e) {
            log.error("Failed to send email notification to: {}", email, e);
        }
    }
    
    /**
     * Send SMS notification to customer.
     * TODO: Integrate with SMS provider (e.g., Twilio)
     */
    private void sendSmsNotification(String phone, String trackingId, ShipmentStatus status) {
        log.info("SMS notification would be sent to: {} for tracking ID: {}", phone, trackingId);
        // TODO: Implement SMS sending logic with Twilio or similar service
    }
    
    /**
     * Build email body for notification.
     */
    private String buildEmailBody(String trackingId, ShipmentStatus status) {
        return String.format("""
                Dear Customer,
                
                Your parcel with tracking ID %s has been updated.
                
                Current Status: %s
                
                You can track your parcel at: http://localhost:8080/api/v1/tracking/%s
                
                Thank you for choosing our service!
                
                Best regards,
                Parcel Tracking Team
                """, trackingId, status.getDescription(), trackingId);
    }
}

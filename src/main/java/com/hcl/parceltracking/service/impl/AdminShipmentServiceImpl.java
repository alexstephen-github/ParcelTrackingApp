package com.hcl.parceltracking.service.impl;

import com.hcl.parceltracking.dto.request.CreateShipmentRequest;
import com.hcl.parceltracking.dto.request.UpdateShipmentRequest;
import com.hcl.parceltracking.dto.response.ShipmentCreateResponse;
import com.hcl.parceltracking.dto.response.ShipmentUpdateResponse;
import com.hcl.parceltracking.exception.ResourceNotFoundException;
import com.hcl.parceltracking.model.Shipment;
import com.hcl.parceltracking.model.ShipmentStatus;
import com.hcl.parceltracking.model.StatusHistory;
import com.hcl.parceltracking.repository.ShipmentRepository;
import com.hcl.parceltracking.repository.StatusHistoryRepository;
import com.hcl.parceltracking.service.AdminShipmentService;
import com.hcl.parceltracking.service.BarcodeGenerationService;
import com.hcl.parceltracking.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Implementation of AdminShipmentService.
 * Handles shipment creation and status updates with notification triggers.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AdminShipmentServiceImpl implements AdminShipmentService {
    
    private final ShipmentRepository shipmentRepository;
    private final StatusHistoryRepository statusHistoryRepository;
    private final BarcodeGenerationService barcodeGenerationService;
    private final NotificationService notificationService;
    
    @Override
    public ShipmentCreateResponse createShipment(CreateShipmentRequest request) {
        log.info("Creating new shipment from {} to {}", request.origin(), request.destination());
        
        // Generate unique tracking ID
        String trackingId = barcodeGenerationService.generateUniqueTrackingId();
        
        Shipment shipment = new Shipment();
        shipment.setTrackingId(trackingId);
        shipment.setStatus(ShipmentStatus.BOOKED);
        shipment.setCustomerEmail(request.customerEmail());
        shipment.setCustomerPhone(request.customerPhone());
        shipment.setOrigin(request.origin());
        shipment.setDestination(request.destination());
        shipment.setEstimatedDelivery(request.estimatedDelivery());
        shipment.setCreatedAt(LocalDateTime.now());
        
        shipmentRepository.save(shipment);
        
        // Create initial status history
        StatusHistory history = new StatusHistory(shipment, null, ShipmentStatus.BOOKED, request.origin());
        statusHistoryRepository.save(history);
        
        log.info("Shipment created successfully with tracking ID: {}", trackingId);
        
        return new ShipmentCreateResponse(
                trackingId, 
                "Shipment created successfully", 
                shipment.getCreatedAt()
        );
    }
    
    @Override
    @CacheEvict(value = "trackingCache", key = "#trackingId")
    public ShipmentUpdateResponse updateShipment(String trackingId, UpdateShipmentRequest request) {
        log.info("Updating shipment {} to status: {}", trackingId, request.status());
        
        Shipment shipment = shipmentRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found: " + trackingId));
        
        // Save old status for history
        ShipmentStatus oldStatus = shipment.getStatus();
        
        // Update shipment
        shipment.setStatus(request.status());
        shipment.setCurrentLocation(request.location());
        shipment.setLastUpdate(LocalDateTime.now());
        
        // Save status history
        StatusHistory history = new StatusHistory(shipment, oldStatus, request.status(), request.location());
        statusHistoryRepository.save(history);
        
        shipmentRepository.save(shipment);
        
        // Trigger notification if significant status change
        if (isSignificantStatusChange(oldStatus, request.status())) {
            log.info("Triggering notification for tracking ID: {}", trackingId);
            notificationService.sendStatusUpdateNotification(shipment, request.status());
        }
        
        log.info("Shipment {} updated successfully", trackingId);
        
        return new ShipmentUpdateResponse(
                trackingId, 
                "Shipment updated successfully", 
                LocalDateTime.now()
        );
    }
    
    /**
     * Check if status change is significant enough to trigger notification.
     */
    private boolean isSignificantStatusChange(ShipmentStatus oldStatus, ShipmentStatus newStatus) {
        return newStatus == ShipmentStatus.OUT_FOR_DELIVERY || 
               newStatus == ShipmentStatus.DELIVERED ||
               newStatus == ShipmentStatus.DELAYED;
    }
}

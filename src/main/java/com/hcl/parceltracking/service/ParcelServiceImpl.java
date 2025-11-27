package com.hcl.parceltracking.service;

import com.hcl.parceltracking.dto.*;
import com.hcl.parceltracking.exception.ResourceNotFoundException;
import com.hcl.parceltracking.model.Parcel;
import com.hcl.parceltracking.model.ParcelStatus;
import com.hcl.parceltracking.model.StatusHistory;
import com.hcl.parceltracking.repository.ParcelRepository;
import com.hcl.parceltracking.repository.StatusHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Implementation of ParcelService for parcel operations.
 * 
 * This service handles all business logic for parcel tracking and management,
 * including caching, transactions, and notifications.
 * 
 * @author HCL Technologies
 * @version 1.0.0
 */
@Service
public class ParcelServiceImpl implements ParcelService {

    private static final Logger logger = LoggerFactory.getLogger(ParcelServiceImpl.class);
    private static final String TRACKING_ID_PREFIX = "TRK";
    private static final int TRACKING_ID_LENGTH = 12;

    private final ParcelRepository parcelRepository;
    private final StatusHistoryRepository statusHistoryRepository;
    private final BarcodeService barcodeService;
    private final NotificationService notificationService;

    public ParcelServiceImpl(ParcelRepository parcelRepository,
                           StatusHistoryRepository statusHistoryRepository,
                           BarcodeService barcodeService,
                           NotificationService notificationService) {
        this.parcelRepository = parcelRepository;
        this.statusHistoryRepository = statusHistoryRepository;
        this.barcodeService = barcodeService;
        this.notificationService = notificationService;
    }

    /**
     * Track a parcel by its tracking ID.
     * 
     * This method is cached to improve performance for frequently tracked parcels.
     * 
     * @param trackingId the unique tracking identifier
     * @return parcel tracking response with current status and history
     * @throws ResourceNotFoundException if tracking ID not found
     */
    @Override
    @Cacheable(value = "parcels", key = "#trackingId")
    @Transactional(readOnly = true)
    public ParcelTrackingResponse trackParcel(String trackingId) {
        logger.debug("Tracking parcel with ID: {}", trackingId);

        Parcel parcel = parcelRepository.findByTrackingIdWithHistory(trackingId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Parcel with tracking ID '" + trackingId + "' not found"));

        return mapToTrackingResponse(parcel);
    }

    /**
     * Create a new parcel shipment.
     * 
     * Generates a unique tracking ID and barcode for the new parcel.
     * 
     * @param request the parcel creation request
     * @return parcel creation response with tracking ID and barcode
     */
    @Override
    @Transactional
    public ParcelCreateResponse createParcel(ParcelCreateRequest request) {
        logger.info("Creating new parcel from {} to {}", 
                   request.getOriginLocation(), request.getDestinationLocation());

        // Generate unique tracking ID
        String trackingId = generateUniqueTrackingId();

        // Generate barcode
        String barcode = barcodeService.generateBarcode(trackingId);

        // Create parcel entity
        Parcel parcel = new Parcel();
        parcel.setTrackingId(trackingId);
        parcel.setBarcode(barcode);
        parcel.setCurrentStatus(ParcelStatus.BOOKED);
        parcel.setCurrentLocation(request.getOriginLocation());
        parcel.setOriginLocation(request.getOriginLocation());
        parcel.setDestinationLocation(request.getDestinationLocation());
        parcel.setRecipientName(request.getRecipientName());
        parcel.setRecipientAddress(request.getRecipientAddress());
        parcel.setEstimatedDeliveryDate(request.getEstimatedDeliveryDate());

        // Create initial status history
        StatusHistory initialHistory = new StatusHistory();
        initialHistory.setStatus(ParcelStatus.BOOKED);
        initialHistory.setLocation(request.getOriginLocation());
        initialHistory.setTimestamp(LocalDateTime.now());
        initialHistory.setNotes("Parcel booked and registered in system");
        parcel.addStatusHistory(initialHistory);

        // Save parcel
        Parcel savedParcel = parcelRepository.save(parcel);

        // Send notification
        notificationService.sendStatusChangeNotification(
                trackingId, ParcelStatus.BOOKED, request.getOriginLocation());

        logger.info("Successfully created parcel with tracking ID: {}", trackingId);

        return new ParcelCreateResponse(
                savedParcel.getTrackingId(),
                savedParcel.getBarcode(),
                savedParcel.getCurrentStatus(),
                "Parcel created successfully",
                savedParcel.getCreatedAt()
        );
    }

    /**
     * Update the status and location of a parcel.
     * 
     * This method evicts the cache entry for the updated parcel.
     * 
     * @param trackingId the unique tracking identifier
     * @param request the status update request
     * @return updated parcel tracking response
     * @throws ResourceNotFoundException if tracking ID not found
     */
    @Override
    @Transactional
    @CacheEvict(value = "parcels", key = "#trackingId")
    public ParcelTrackingResponse updateParcelStatus(String trackingId, ParcelStatusUpdateRequest request) {
        logger.info("Updating status for parcel {} to {}", trackingId, request.getStatus());

        Parcel parcel = parcelRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Parcel with tracking ID '" + trackingId + "' not found"));

        // Update parcel status and location
        parcel.setCurrentStatus(request.getStatus());
        parcel.setCurrentLocation(request.getLocation());

        // Add status history entry
        StatusHistory statusHistory = new StatusHistory();
        statusHistory.setStatus(request.getStatus());
        statusHistory.setLocation(request.getLocation());
        statusHistory.setTimestamp(LocalDateTime.now());
        statusHistory.setNotes(request.getNotes());
        parcel.addStatusHistory(statusHistory);

        // Save parcel
        Parcel updatedParcel = parcelRepository.save(parcel);

        // Send notification
        notificationService.sendStatusChangeNotification(
                trackingId, request.getStatus(), request.getLocation());

        logger.info("Successfully updated parcel {} to status {}", trackingId, request.getStatus());

        return mapToTrackingResponse(updatedParcel);
    }

    /**
     * Get all parcels with optional status filter.
     * 
     * @param pageable pagination information
     * @param status optional status filter
     * @return page of parcel tracking responses
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ParcelTrackingResponse> getAllParcels(Pageable pageable, ParcelStatus status) {
        logger.debug("Retrieving all parcels with status filter: {}", status);

        Page<Parcel> parcels;
        if (status != null) {
            parcels = parcelRepository.findByCurrentStatus(status, pageable);
        } else {
            parcels = parcelRepository.findAll(pageable);
        }

        return parcels.map(this::mapToTrackingResponse);
    }

    /**
     * Delete a parcel by tracking ID.
     * 
     * @param trackingId the unique tracking identifier
     * @throws ResourceNotFoundException if tracking ID not found
     */
    @Override
    @Transactional
    @CacheEvict(value = "parcels", key = "#trackingId")
    public void deleteParcel(String trackingId) {
        logger.info("Deleting parcel with tracking ID: {}", trackingId);

        Parcel parcel = parcelRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Parcel with tracking ID '" + trackingId + "' not found"));

        parcelRepository.delete(parcel);
        logger.info("Successfully deleted parcel: {}", trackingId);
    }

    /**
     * Generate a unique tracking ID.
     * 
     * Format: TRK + 12 alphanumeric characters
     * 
     * @return unique tracking ID
     */
    private String generateUniqueTrackingId() {
        String trackingId;
        do {
            trackingId = TRACKING_ID_PREFIX + generateRandomAlphanumeric(TRACKING_ID_LENGTH);
        } while (parcelRepository.existsByTrackingId(trackingId));
        return trackingId;
    }

    /**
     * Generate random alphanumeric string.
     * 
     * @param length the length of the string to generate
     * @return random alphanumeric string
     */
    private String generateRandomAlphanumeric(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    /**
     * Map Parcel entity to ParcelTrackingResponse DTO.
     * 
     * @param parcel the parcel entity
     * @return parcel tracking response DTO
     */
    private ParcelTrackingResponse mapToTrackingResponse(Parcel parcel) {
        List<StatusHistoryDto> historyDtos = parcel.getStatusHistory().stream()
                .map(history -> new StatusHistoryDto(
                        history.getStatus(),
                        history.getLocation(),
                        history.getTimestamp(),
                        history.getNotes()
                ))
                .collect(Collectors.toList());

        return new ParcelTrackingResponse(
                parcel.getTrackingId(),
                parcel.getCurrentStatus(),
                parcel.getCurrentLocation(),
                parcel.getLastUpdatedAt(),
                parcel.getEstimatedDeliveryDate(),
                historyDtos
        );
    }
}

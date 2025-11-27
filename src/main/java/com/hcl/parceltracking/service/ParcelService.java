package com.hcl.parceltracking.service;

import com.hcl.parceltracking.dto.*;
import com.hcl.parceltracking.model.ParcelStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for parcel operations.
 * 
 * Defines the business logic for parcel tracking and management.
 * 
 * @author HCL Technologies
 * @version 1.0.0
 */
public interface ParcelService {

    /**
     * Track a parcel by its tracking ID.
     * 
     * @param trackingId the unique tracking identifier
     * @return parcel tracking response with current status and history
     * @throws com.hcl.parceltracking.exception.ResourceNotFoundException if tracking ID not found
     */
    ParcelTrackingResponse trackParcel(String trackingId);

    /**
     * Create a new parcel shipment.
     * 
     * @param request the parcel creation request
     * @return parcel creation response with tracking ID and barcode
     */
    ParcelCreateResponse createParcel(ParcelCreateRequest request);

    /**
     * Update the status and location of a parcel.
     * 
     * @param trackingId the unique tracking identifier
     * @param request the status update request
     * @return updated parcel tracking response
     * @throws com.hcl.parceltracking.exception.ResourceNotFoundException if tracking ID not found
     */
    ParcelTrackingResponse updateParcelStatus(String trackingId, ParcelStatusUpdateRequest request);

    /**
     * Get all parcels with optional status filter.
     * 
     * @param pageable pagination information
     * @param status optional status filter
     * @return page of parcel tracking responses
     */
    Page<ParcelTrackingResponse> getAllParcels(Pageable pageable, ParcelStatus status);

    /**
     * Delete a parcel by tracking ID.
     * 
     * @param trackingId the unique tracking identifier
     * @throws com.hcl.parceltracking.exception.ResourceNotFoundException if tracking ID not found
     */
    void deleteParcel(String trackingId);
}

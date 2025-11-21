package com.hcl.parceltracking.service;

import com.hcl.parceltracking.dto.request.CreateShipmentRequest;
import com.hcl.parceltracking.dto.request.UpdateShipmentRequest;
import com.hcl.parceltracking.dto.response.ShipmentCreateResponse;
import com.hcl.parceltracking.dto.response.ShipmentUpdateResponse;

/**
 * Service interface for admin shipment management operations.
 */
public interface AdminShipmentService {
    
    /**
     * Create a new shipment with auto-generated tracking ID.
     * 
     * @param request the shipment creation request
     * @return shipment creation response with tracking ID
     */
    ShipmentCreateResponse createShipment(CreateShipmentRequest request);
    
    /**
     * Update shipment status and location.
     * 
     * @param trackingId the tracking ID of the shipment
     * @param request the update request
     * @return shipment update response
     */
    ShipmentUpdateResponse updateShipment(String trackingId, UpdateShipmentRequest request);
}

package com.hcl.parceltracking.service;

/**
 * Service interface for barcode generation operations.
 */
public interface BarcodeGenerationService {
    
    /**
     * Generate a unique tracking ID (barcode) for a new shipment.
     * 
     * @return unique tracking ID
     */
    String generateUniqueTrackingId();
}

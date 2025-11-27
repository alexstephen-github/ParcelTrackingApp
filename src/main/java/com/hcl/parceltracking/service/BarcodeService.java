package com.hcl.parceltracking.service;

/**
 * Service interface for barcode generation.
 * 
 * @author HCL Technologies
 * @version 1.0.0
 */
public interface BarcodeService {

    /**
     * Generate a barcode for a given tracking ID.
     * 
     * @param trackingId the tracking identifier to encode
     * @return Base64 encoded barcode image
     */
    String generateBarcode(String trackingId);
}

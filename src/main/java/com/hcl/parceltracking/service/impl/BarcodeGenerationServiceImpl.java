package com.hcl.parceltracking.service.impl;

import com.hcl.parceltracking.repository.ShipmentRepository;
import com.hcl.parceltracking.service.BarcodeGenerationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

/**
 * Implementation of BarcodeGenerationService.
 * Generates unique alphanumeric tracking IDs for shipments.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BarcodeGenerationServiceImpl implements BarcodeGenerationService {
    
    private final ShipmentRepository shipmentRepository;
    private final SecureRandom random = new SecureRandom();
    
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int TRACKING_ID_LENGTH = 12;
    
    @Override
    public String generateUniqueTrackingId() {
        String trackingId;
        int attempts = 0;
        int maxAttempts = 10;
        
        do {
            trackingId = generateRandomAlphanumeric(TRACKING_ID_LENGTH);
            attempts++;
            
            if (attempts > maxAttempts) {
                log.warn("Failed to generate unique tracking ID after {} attempts", maxAttempts);
                throw new RuntimeException("Failed to generate unique tracking ID");
            }
        } while (shipmentRepository.existsByTrackingId(trackingId));
        
        log.debug("Generated unique tracking ID: {} in {} attempt(s)", trackingId, attempts);
        
        return trackingId;
    }
    
    /**
     * Generate random alphanumeric string of specified length.
     */
    private String generateRandomAlphanumeric(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}

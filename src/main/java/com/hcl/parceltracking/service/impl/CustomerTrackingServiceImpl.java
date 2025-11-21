package com.hcl.parceltracking.service.impl;

import com.hcl.parceltracking.dto.response.TrackingResponse;
import com.hcl.parceltracking.exception.ResourceNotFoundException;
import com.hcl.parceltracking.mapper.ShipmentMapper;
import com.hcl.parceltracking.model.Shipment;
import com.hcl.parceltracking.repository.ShipmentRepository;
import com.hcl.parceltracking.service.CustomerTrackingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of CustomerTrackingService.
 * Provides tracking information retrieval with caching support.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CustomerTrackingServiceImpl implements CustomerTrackingService {
    
    private final ShipmentRepository shipmentRepository;
    private final ShipmentMapper mapper;
    
    @Override
    @Cacheable(value = "trackingCache", key = "#trackingId")
    public TrackingResponse getTrackingInfo(String trackingId) {
        log.info("Fetching tracking information for tracking ID: {}", trackingId);
        
        Shipment shipment = shipmentRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new ResourceNotFoundException("Tracking ID not found: " + trackingId));
        
        log.debug("Found shipment: {} with status: {}", trackingId, shipment.getStatus());
        
        return mapper.toTrackingResponse(shipment);
    }
}

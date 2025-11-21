package com.hcl.parceltracking.controller;

import com.hcl.parceltracking.dto.response.TrackingResponse;
import com.hcl.parceltracking.service.CustomerTrackingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for customer tracking operations.
 * Provides public endpoints for tracking parcel status.
 */
@RestController
@RequestMapping("/api/v1/tracking")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Customer Tracking", description = "APIs for customers to track their parcels")
public class CustomerTrackingController {
    
    private final CustomerTrackingService trackingService;
    
    /**
     * Track parcel by tracking ID.
     * 
     * @param trackingId the tracking ID (10-15 alphanumeric characters)
     * @return tracking response with current status and history
     */
    @GetMapping("/{trackingId}")
    @Operation(
        summary = "Track parcel by tracking ID", 
        description = "Returns current status, location, estimated delivery, and status history for a parcel"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tracking information retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Invalid tracking ID - parcel not found"),
        @ApiResponse(responseCode = "429", description = "Too many requests - rate limit exceeded")
    })
    public ResponseEntity<TrackingResponse> trackParcel(
            @Parameter(description = "Tracking ID (10-15 alphanumeric characters)", example = "ABC123456789")
            @PathVariable 
            @Pattern(regexp = "^[A-Z0-9]{10,15}$", message = "Invalid tracking ID format") 
            String trackingId) {
        
        log.info("Tracking request received for ID: {}", trackingId);
        
        TrackingResponse response = trackingService.getTrackingInfo(trackingId);
        
        return ResponseEntity.ok(response);
    }
}

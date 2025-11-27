package com.hcl.parceltracking.controller;

import com.hcl.parceltracking.dto.ParcelTrackingResponse;
import com.hcl.parceltracking.service.ParcelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for customer-facing parcel tracking operations.
 * 
 * Provides endpoints for tracking parcels by their unique tracking ID.
 * 
 * @author HCL Technologies
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/parcels")
@Validated
@Tag(name = "Parcel Tracking", description = "Customer endpoints for tracking parcels")
public class ParcelController {

    private static final Logger logger = LoggerFactory.getLogger(ParcelController.class);

    private final ParcelService parcelService;

    public ParcelController(ParcelService parcelService) {
        this.parcelService = parcelService;
    }

    /**
     * Track a parcel by its tracking ID.
     * 
     * @param trackingId the unique tracking identifier
     * @return parcel tracking response with current status and history
     */
    @Operation(
        summary = "Track parcel by ID",
        description = "Retrieve current status, location, and complete history of a parcel using its tracking ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Parcel found successfully",
            content = @Content(schema = @Schema(implementation = ParcelTrackingResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid tracking ID format"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Parcel not found with the given tracking ID"
        )
    })
    @GetMapping("/track/{trackingId}")
    public ResponseEntity<ParcelTrackingResponse> trackParcel(
            @Parameter(description = "Unique tracking identifier (10-15 alphanumeric characters)", required = true)
            @PathVariable
            @Size(min = 10, max = 15, message = "Tracking ID must be between 10 and 15 characters")
            @Pattern(regexp = "^[A-Z0-9]+$", message = "Tracking ID must be alphanumeric")
            String trackingId) {
        
        logger.info("Received tracking request for ID: {}", trackingId);
        ParcelTrackingResponse response = parcelService.trackParcel(trackingId);
        return ResponseEntity.ok(response);
    }

    /**
     * Health check endpoint for the controller.
     * 
     * @return simple health status
     */
    @Operation(
        summary = "Health check",
        description = "Check if the parcel tracking service is operational"
    )
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Parcel Tracking Service is running");
    }
}

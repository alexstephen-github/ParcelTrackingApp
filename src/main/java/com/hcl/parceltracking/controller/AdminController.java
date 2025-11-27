package com.hcl.parceltracking.controller;

import com.hcl.parceltracking.dto.ParcelCreateRequest;
import com.hcl.parceltracking.dto.ParcelCreateResponse;
import com.hcl.parceltracking.dto.ParcelStatusUpdateRequest;
import com.hcl.parceltracking.dto.ParcelTrackingResponse;
import com.hcl.parceltracking.model.ParcelStatus;
import com.hcl.parceltracking.service.ParcelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for administrative parcel management operations.
 * 
 * Provides endpoints for creating parcels, updating status, and managing shipments.
 * 
 * @author HCL Technologies
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/admin/parcels")
@Validated
@Tag(name = "Admin - Parcel Management", description = "Administrative endpoints for managing parcels")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final ParcelService parcelService;

    public AdminController(ParcelService parcelService) {
        this.parcelService = parcelService;
    }

    /**
     * Create a new parcel shipment.
     * 
     * @param request the parcel creation request
     * @return parcel creation response with tracking ID and barcode
     */
    @Operation(
        summary = "Create new parcel",
        description = "Register a new parcel shipment with auto-generated tracking ID and barcode"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Parcel created successfully",
            content = @Content(schema = @Schema(implementation = ParcelCreateResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request data"
        )
    })
    @PostMapping
    public ResponseEntity<ParcelCreateResponse> createParcel(
            @Valid @RequestBody ParcelCreateRequest request) {
        
        logger.info("Received request to create new parcel");
        ParcelCreateResponse response = parcelService.createParcel(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Update the status and location of a parcel.
     * 
     * @param trackingId the unique tracking identifier
     * @param request the status update request
     * @return updated parcel tracking response
     */
    @Operation(
        summary = "Update parcel status",
        description = "Update the status and location of an existing parcel"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Status updated successfully",
            content = @Content(schema = @Schema(implementation = ParcelTrackingResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request data"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Parcel not found"
        )
    })
    @PutMapping("/{trackingId}/status")
    public ResponseEntity<ParcelTrackingResponse> updateParcelStatus(
            @Parameter(description = "Unique tracking identifier", required = true)
            @PathVariable
            @Size(min = 10, max = 15, message = "Tracking ID must be between 10 and 15 characters")
            @Pattern(regexp = "^[A-Z0-9]+$", message = "Tracking ID must be alphanumeric")
            String trackingId,
            @Valid @RequestBody ParcelStatusUpdateRequest request) {
        
        logger.info("Received request to update status for parcel: {}", trackingId);
        ParcelTrackingResponse response = parcelService.updateParcelStatus(trackingId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all parcels with pagination and optional status filter.
     * 
     * @param page page number (default: 0)
     * @param size page size (default: 20)
     * @param status optional status filter
     * @return page of parcel tracking responses
     */
    @Operation(
        summary = "Get all parcels",
        description = "Retrieve all parcels with pagination and optional status filter"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Parcels retrieved successfully"
        )
    })
    @GetMapping
    public ResponseEntity<Page<ParcelTrackingResponse>> getAllParcels(
            @Parameter(description = "Page number (0-indexed)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Filter by status")
            @RequestParam(required = false) ParcelStatus status) {
        
        logger.info("Received request to get all parcels - page: {}, size: {}, status: {}", 
                   page, size, status);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ParcelTrackingResponse> response = parcelService.getAllParcels(pageable, status);
        return ResponseEntity.ok(response);
    }

    /**
     * Get a specific parcel by tracking ID.
     * 
     * @param trackingId the unique tracking identifier
     * @return parcel tracking response
     */
    @Operation(
        summary = "Get parcel by ID",
        description = "Retrieve detailed information about a specific parcel"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Parcel found successfully",
            content = @Content(schema = @Schema(implementation = ParcelTrackingResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Parcel not found"
        )
    })
    @GetMapping("/{trackingId}")
    public ResponseEntity<ParcelTrackingResponse> getParcel(
            @Parameter(description = "Unique tracking identifier", required = true)
            @PathVariable
            @Size(min = 10, max = 15, message = "Tracking ID must be between 10 and 15 characters")
            @Pattern(regexp = "^[A-Z0-9]+$", message = "Tracking ID must be alphanumeric")
            String trackingId) {
        
        logger.info("Admin retrieving parcel: {}", trackingId);
        ParcelTrackingResponse response = parcelService.trackParcel(trackingId);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a parcel by tracking ID.
     * 
     * @param trackingId the unique tracking identifier
     * @return no content response
     */
    @Operation(
        summary = "Delete parcel",
        description = "Delete a parcel from the system"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Parcel deleted successfully"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Parcel not found"
        )
    })
    @DeleteMapping("/{trackingId}")
    public ResponseEntity<Void> deleteParcel(
            @Parameter(description = "Unique tracking identifier", required = true)
            @PathVariable
            @Size(min = 10, max = 15, message = "Tracking ID must be between 10 and 15 characters")
            @Pattern(regexp = "^[A-Z0-9]+$", message = "Tracking ID must be alphanumeric")
            String trackingId) {
        
        logger.info("Received request to delete parcel: {}", trackingId);
        parcelService.deleteParcel(trackingId);
        return ResponseEntity.noContent().build();
    }
}

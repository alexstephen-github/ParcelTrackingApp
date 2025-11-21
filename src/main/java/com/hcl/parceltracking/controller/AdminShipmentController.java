package com.hcl.parceltracking.controller;

import com.hcl.parceltracking.dto.request.CreateShipmentRequest;
import com.hcl.parceltracking.dto.request.UpdateShipmentRequest;
import com.hcl.parceltracking.dto.response.ShipmentCreateResponse;
import com.hcl.parceltracking.dto.response.ShipmentUpdateResponse;
import com.hcl.parceltracking.service.AdminShipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for admin shipment management operations.
 * Provides secured endpoints for creating and updating shipments.
 */
@RestController
@RequestMapping("/api/v1/admin/shipments")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearer-jwt")
@Tag(name = "Admin Shipment Management", description = "APIs for administrators to manage shipments")
public class AdminShipmentController {
    
    private final AdminShipmentService shipmentService;
    
    /**
     * Create a new shipment with auto-generated tracking ID.
     * 
     * @param request the shipment creation request
     * @return shipment creation response with tracking ID
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Create new shipment", 
        description = "Creates a new shipment with auto-generated barcode/tracking ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Shipment created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized - authentication required"),
        @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions")
    })
    public ResponseEntity<ShipmentCreateResponse> createShipment(
            @Valid @RequestBody CreateShipmentRequest request) {
        
        log.info("Shipment creation request received from {} to {}", 
                request.origin(), request.destination());
        
        ShipmentCreateResponse response = shipmentService.createShipment(request);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Update shipment status and location.
     * 
     * @param trackingId the tracking ID of the shipment
     * @param request the update request
     * @return shipment update response
     */
    @PutMapping("/{trackingId}/status")
    @Operation(
        summary = "Update shipment status", 
        description = "Update status and location of a shipment. Triggers notifications for significant status changes."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Shipment updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "Shipment not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions")
    })
    public ResponseEntity<ShipmentUpdateResponse> updateShipmentStatus(
            @Parameter(description = "Tracking ID of the shipment", example = "ABC123456789")
            @PathVariable String trackingId,
            @Valid @RequestBody UpdateShipmentRequest request) {
        
        log.info("Shipment update request received for tracking ID: {}", trackingId);
        
        ShipmentUpdateResponse response = shipmentService.updateShipment(trackingId, request);
        
        return ResponseEntity.ok(response);
    }
}

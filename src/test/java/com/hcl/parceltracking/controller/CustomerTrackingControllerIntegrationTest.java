package com.hcl.parceltracking.controller;

import com.hcl.parceltracking.model.Shipment;
import com.hcl.parceltracking.model.ShipmentStatus;
import com.hcl.parceltracking.repository.ShipmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for CustomerTrackingController.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers
class CustomerTrackingControllerIntegrationTest {
    
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ShipmentRepository shipmentRepository;
    
    @BeforeEach
    void setUp() {
        shipmentRepository.deleteAll();
    }
    
    @Test
    void trackParcel_ValidTrackingId_ReturnsOk() throws Exception {
        // Given
        Shipment shipment = new Shipment();
        shipment.setTrackingId("TEST12345678");
        shipment.setStatus(ShipmentStatus.IN_TRANSIT);
        shipment.setCurrentLocation("New York");
        shipment.setOrigin("Boston");
        shipment.setDestination("Los Angeles");
        shipment.setCreatedAt(LocalDateTime.now());
        shipmentRepository.save(shipment);
        
        // When & Then
        mockMvc.perform(get("/api/v1/tracking/TEST12345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trackingId").value("TEST12345678"))
                .andExpect(jsonPath("$.status").value("IN_TRANSIT"))
                .andExpect(jsonPath("$.currentLocation").value("New York"));
    }
    
    @Test
    void trackParcel_InvalidTrackingId_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/tracking/INVALID123"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(containsString("Tracking ID not found")));
    }
}

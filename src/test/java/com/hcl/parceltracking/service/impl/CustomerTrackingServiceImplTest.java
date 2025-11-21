package com.hcl.parceltracking.service.impl;

import com.hcl.parceltracking.dto.response.TrackingResponse;
import com.hcl.parceltracking.exception.ResourceNotFoundException;
import com.hcl.parceltracking.mapper.ShipmentMapper;
import com.hcl.parceltracking.model.Shipment;
import com.hcl.parceltracking.model.ShipmentStatus;
import com.hcl.parceltracking.repository.ShipmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CustomerTrackingServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
class CustomerTrackingServiceImplTest {
    
    @Mock
    private ShipmentRepository shipmentRepository;
    
    @Mock
    private ShipmentMapper mapper;
    
    @InjectMocks
    private CustomerTrackingServiceImpl trackingService;
    
    private Shipment testShipment;
    private TrackingResponse expectedResponse;
    
    @BeforeEach
    void setUp() {
        testShipment = new Shipment();
        testShipment.setId(1L);
        testShipment.setTrackingId("ABC123456789");
        testShipment.setStatus(ShipmentStatus.IN_TRANSIT);
        testShipment.setCurrentLocation("New York");
        testShipment.setOrigin("Boston");
        testShipment.setDestination("Los Angeles");
        testShipment.setCreatedAt(LocalDateTime.now());
        testShipment.setLastUpdate(LocalDateTime.now());
        
        expectedResponse = new TrackingResponse(
                "ABC123456789",
                "New York",
                ShipmentStatus.IN_TRANSIT,
                LocalDateTime.now(),
                LocalDate.now().plusDays(2),
                Collections.emptyList()
        );
    }
    
    @Test
    void getTrackingInfo_ValidTrackingId_ReturnsTrackingResponse() {
        // Given
        String trackingId = "ABC123456789";
        when(shipmentRepository.findByTrackingId(trackingId)).thenReturn(Optional.of(testShipment));
        when(mapper.toTrackingResponse(testShipment)).thenReturn(expectedResponse);
        
        // When
        TrackingResponse result = trackingService.getTrackingInfo(trackingId);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.trackingId()).isEqualTo(trackingId);
        assertThat(result.status()).isEqualTo(ShipmentStatus.IN_TRANSIT);
        assertThat(result.currentLocation()).isEqualTo("New York");
        
        verify(shipmentRepository).findByTrackingId(trackingId);
        verify(mapper).toTrackingResponse(testShipment);
    }
    
    @Test
    void getTrackingInfo_InvalidTrackingId_ThrowsResourceNotFoundException() {
        // Given
        String trackingId = "INVALID123";
        when(shipmentRepository.findByTrackingId(trackingId)).thenReturn(Optional.empty());
        
        // When & Then
        assertThatThrownBy(() -> trackingService.getTrackingInfo(trackingId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Tracking ID not found");
        
        verify(shipmentRepository).findByTrackingId(trackingId);
        verifyNoInteractions(mapper);
    }
}

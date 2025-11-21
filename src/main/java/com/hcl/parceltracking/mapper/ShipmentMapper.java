package com.hcl.parceltracking.mapper;

import com.hcl.parceltracking.dto.response.StatusHistoryDto;
import com.hcl.parceltracking.dto.response.TrackingResponse;
import com.hcl.parceltracking.model.Shipment;
import com.hcl.parceltracking.model.StatusHistory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper class for converting between Shipment entities and DTOs.
 */
@Component
public class ShipmentMapper {
    
    /**
     * Convert Shipment entity to TrackingResponse DTO.
     * 
     * @param shipment the shipment entity
     * @return tracking response DTO
     */
    public TrackingResponse toTrackingResponse(Shipment shipment) {
        List<StatusHistoryDto> history = shipment.getStatusHistory()
                .stream()
                .map(this::toStatusHistoryDto)
                .collect(Collectors.toList());
        
        return new TrackingResponse(
                shipment.getTrackingId(),
                shipment.getCurrentLocation(),
                shipment.getStatus(),
                shipment.getLastUpdate(),
                shipment.getEstimatedDelivery(),
                history
        );
    }
    
    /**
     * Convert StatusHistory entity to StatusHistoryDto.
     * 
     * @param statusHistory the status history entity
     * @return status history DTO
     */
    private StatusHistoryDto toStatusHistoryDto(StatusHistory statusHistory) {
        return new StatusHistoryDto(
                statusHistory.getNewStatus(),
                statusHistory.getLocation(),
                statusHistory.getTimestamp()
        );
    }
}

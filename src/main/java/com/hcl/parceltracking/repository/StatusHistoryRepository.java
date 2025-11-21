package com.hcl.parceltracking.repository;

import com.hcl.parceltracking.model.StatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for StatusHistory entity.
 * Provides database access methods for status history operations.
 */
@Repository
public interface StatusHistoryRepository extends JpaRepository<StatusHistory, Long> {
    
    /**
     * Find all status history records for a specific shipment.
     * 
     * @param shipmentId the shipment ID
     * @return list of status history records ordered by timestamp descending
     */
    List<StatusHistory> findByShipmentIdOrderByTimestampDesc(Long shipmentId);
    
    /**
     * Find all status history records by tracking ID.
     * 
     * @param trackingId the tracking ID
     * @return list of status history records
     */
    List<StatusHistory> findByShipment_TrackingIdOrderByTimestampDesc(String trackingId);
}

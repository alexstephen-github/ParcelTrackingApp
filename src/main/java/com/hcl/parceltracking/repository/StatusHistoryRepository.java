package com.hcl.parceltracking.repository;

import com.hcl.parceltracking.model.StatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for StatusHistory entity.
 * 
 * Provides CRUD operations and custom queries for status history data access.
 * 
 * @author HCL Technologies
 * @version 1.0.0
 */
@Repository
public interface StatusHistoryRepository extends JpaRepository<StatusHistory, UUID> {

    /**
     * Find all status history entries for a specific parcel.
     * 
     * @param parcelId the UUID of the parcel
     * @return list of status history entries ordered by timestamp
     */
    List<StatusHistory> findByParcelIdOrderByTimestampAsc(UUID parcelId);

    /**
     * Find all status history entries for a parcel by tracking ID.
     * 
     * @param trackingId the tracking identifier
     * @return list of status history entries ordered by timestamp
     */
    List<StatusHistory> findByParcelTrackingIdOrderByTimestampAsc(String trackingId);
}

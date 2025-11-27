package com.hcl.parceltracking.repository;

import com.hcl.parceltracking.model.Parcel;
import com.hcl.parceltracking.model.ParcelStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Parcel entity.
 * 
 * Provides CRUD operations and custom queries for parcel data access.
 * 
 * @author HCL Technologies
 * @version 1.0.0
 */
@Repository
public interface ParcelRepository extends JpaRepository<Parcel, UUID> {

    /**
     * Find a parcel by its tracking ID.
     * 
     * @param trackingId the tracking identifier
     * @return Optional containing the parcel if found
     */
    Optional<Parcel> findByTrackingId(String trackingId);

    /**
     * Check if a tracking ID already exists.
     * 
     * @param trackingId the tracking identifier to check
     * @return true if the tracking ID exists, false otherwise
     */
    boolean existsByTrackingId(String trackingId);

    /**
     * Find all parcels with a specific status.
     * 
     * @param status the parcel status to filter by
     * @param pageable pagination information
     * @return page of parcels with the specified status
     */
    Page<Parcel> findByCurrentStatus(ParcelStatus status, Pageable pageable);

    /**
     * Find parcels by destination location.
     * 
     * @param destinationLocation the destination location
     * @param pageable pagination information
     * @return page of parcels with the specified destination
     */
    Page<Parcel> findByDestinationLocation(String destinationLocation, Pageable pageable);

    /**
     * Find a parcel with its status history eagerly loaded.
     * 
     * @param trackingId the tracking identifier
     * @return Optional containing the parcel with status history if found
     */
    @Query("SELECT p FROM Parcel p LEFT JOIN FETCH p.statusHistory WHERE p.trackingId = :trackingId")
    Optional<Parcel> findByTrackingIdWithHistory(String trackingId);
}

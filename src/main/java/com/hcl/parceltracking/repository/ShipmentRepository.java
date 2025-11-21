package com.hcl.parceltracking.repository;

import com.hcl.parceltracking.model.Shipment;
import com.hcl.parceltracking.model.ShipmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Shipment entity.
 * Provides database access methods for shipment operations.
 */
@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    
    /**
     * Find a shipment by its tracking ID.
     * 
     * @param trackingId the tracking ID to search for
     * @return Optional containing the shipment if found
     */
    Optional<Shipment> findByTrackingId(String trackingId);
    
    /**
     * Check if a shipment exists with the given tracking ID.
     * 
     * @param trackingId the tracking ID to check
     * @return true if exists, false otherwise
     */
    boolean existsByTrackingId(String trackingId);
    
    /**
     * Find all shipments with a specific status.
     * 
     * @param status the shipment status to filter by
     * @return list of shipments with the given status
     */
    List<Shipment> findByStatus(ShipmentStatus status);
    
    /**
     * Find all shipments for a specific customer email.
     * 
     * @param email the customer email
     * @return list of shipments for the customer
     */
    List<Shipment> findByCustomerEmail(String email);
    
    /**
     * Find shipments by origin location.
     * 
     * @param origin the origin location
     * @return list of shipments from the origin
     */
    List<Shipment> findByOrigin(String origin);
    
    /**
     * Find shipments by destination location.
     * 
     * @param destination the destination location
     * @return list of shipments to the destination
     */
    List<Shipment> findByDestination(String destination);
}

package com.hcl.parceltracking.model;

/**
 * Enumeration representing the various states a parcel can be in during its journey.
 * 
 * @author HCL Technologies
 * @version 1.0.0
 */
public enum ParcelStatus {
    /**
     * Parcel has been registered in the system
     */
    BOOKED,
    
    /**
     * Parcel is in transit between locations
     */
    IN_TRANSIT,
    
    /**
     * Parcel is out for delivery to the recipient
     */
    OUT_FOR_DELIVERY,
    
    /**
     * Parcel has been successfully delivered
     */
    DELIVERED,
    
    /**
     * Parcel delivery has been delayed
     */
    DELAYED,
    
    /**
     * Parcel shipment has been cancelled
     */
    CANCELLED,
    
    /**
     * Parcel has been returned to sender
     */
    RETURNED
}

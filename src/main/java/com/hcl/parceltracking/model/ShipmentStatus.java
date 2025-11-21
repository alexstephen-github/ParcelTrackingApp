package com.hcl.parceltracking.model;

/**
 * Enumeration of possible shipment statuses.
 * Represents the lifecycle of a parcel from booking to delivery.
 */
public enum ShipmentStatus {
    /**
     * Shipment has been booked and awaiting pickup
     */
    BOOKED("Shipment booked"),
    
    /**
     * Shipment is in transit to destination
     */
    IN_TRANSIT("In transit"),
    
    /**
     * Shipment is out for delivery to the recipient
     */
    OUT_FOR_DELIVERY("Out for delivery"),
    
    /**
     * Shipment has been successfully delivered
     */
    DELIVERED("Delivered"),
    
    /**
     * Shipment delivery has been delayed
     */
    DELAYED("Delayed");
    
    private final String description;
    
    ShipmentStatus(String description) {
        this.description = description;
    }
    
    /**
     * Gets the human-readable description of the status.
     * 
     * @return status description
     */
    public String getDescription() {
        return description;
    }
}

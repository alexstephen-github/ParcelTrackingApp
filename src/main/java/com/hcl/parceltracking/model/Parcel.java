package com.hcl.parceltracking.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * JPA Entity representing a Parcel in the tracking system.
 * 
 * This entity stores all information about a parcel including its current status,
 * location, and delivery details.
 * 
 * @author HCL Technologies
 * @version 1.0.0
 */
@Entity
@Table(name = "parcels")
@Getter
@Setter
@ToString(exclude = "statusHistory")
@NoArgsConstructor
@AllArgsConstructor
public class Parcel {

    /**
     * Unique identifier for the parcel (UUID)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Unique tracking identifier visible to customers (e.g., TRK1234567890)
     */
    @Column(unique = true, nullable = false, length = 15)
    private String trackingId;

    /**
     * Generated barcode for the parcel (Base64 encoded image)
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String barcode;

    /**
     * Current status of the parcel
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ParcelStatus currentStatus;

    /**
     * Current physical location of the parcel
     */
    @Column(nullable = false)
    private String currentLocation;

    /**
     * Origin location where the parcel started its journey
     */
    @Column(nullable = false)
    private String originLocation;

    /**
     * Destination location where the parcel will be delivered
     */
    @Column(nullable = false)
    private String destinationLocation;

    /**
     * Name of the recipient
     */
    @Column(nullable = false)
    private String recipientName;

    /**
     * Full address of the recipient
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String recipientAddress;

    /**
     * Estimated date of delivery
     */
    @Column
    private LocalDate estimatedDeliveryDate;

    /**
     * Timestamp when the parcel was created in the system
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp of the last update to the parcel
     */
    @Column(nullable = false)
    private LocalDateTime lastUpdatedAt;

    /**
     * Complete history of status changes for this parcel
     */
    @OneToMany(mappedBy = "parcel", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<StatusHistory> statusHistory = new ArrayList<>();

    /**
     * JPA callback method to set timestamps before persisting a new entity
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        lastUpdatedAt = LocalDateTime.now();
    }

    /**
     * JPA callback method to update the lastUpdatedAt timestamp before updating
     */
    @PreUpdate
    protected void onUpdate() {
        lastUpdatedAt = LocalDateTime.now();
    }

    /**
     * Convenience method to add a status history entry
     * 
     * @param history the status history entry to add
     */
    public void addStatusHistory(StatusHistory history) {
        statusHistory.add(history);
        history.setParcel(this);
    }
}

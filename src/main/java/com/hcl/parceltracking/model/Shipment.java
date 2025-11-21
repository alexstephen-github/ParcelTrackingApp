package com.hcl.parceltracking.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA Entity representing a shipment/parcel.
 * Contains all information about a parcel being tracked through the system.
 */
@Entity
@Table(name = "shipments", indexes = {
    @Index(name = "idx_tracking_id", columnList = "tracking_id", unique = true),
    @Index(name = "idx_status", columnList = "status")
})
@Getter
@Setter
@ToString(exclude = {"statusHistory"})
@EqualsAndHashCode(exclude = {"id", "statusHistory"})
public class Shipment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "tracking_id", unique = true, nullable = false, length = 15)
    private String trackingId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ShipmentStatus status;
    
    @Column(name = "current_location", length = 255)
    private String currentLocation;
    
    @Column(name = "origin", nullable = false, length = 255)
    private String origin;
    
    @Column(name = "destination", nullable = false, length = 255)
    private String destination;
    
    @Column(name = "customer_email", length = 100)
    private String customerEmail;
    
    @Column(name = "customer_phone", length = 20)
    private String customerPhone;
    
    @Column(name = "estimated_delivery")
    private LocalDate estimatedDelivery;
    
    @Column(name = "last_update")
    private LocalDateTime lastUpdate;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StatusHistory> statusHistory = new ArrayList<>();
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        lastUpdate = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        lastUpdate = LocalDateTime.now();
    }
}

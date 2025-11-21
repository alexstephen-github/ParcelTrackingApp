package com.hcl.parceltracking.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * JPA Entity representing the audit trail of status changes for a shipment.
 * Tracks all status transitions with timestamps and locations.
 */
@Entity
@Table(name = "status_history", indexes = {
    @Index(name = "idx_shipment_id", columnList = "shipment_id"),
    @Index(name = "idx_timestamp", columnList = "timestamp")
})
@Getter
@Setter
public class StatusHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_id", nullable = false)
    private Shipment shipment;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "old_status", length = 50)
    private ShipmentStatus oldStatus;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "new_status", nullable = false, length = 50)
    private ShipmentStatus newStatus;
    
    @Column(name = "location", length = 255)
    private String location;
    
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
    
    public StatusHistory() {
        this.timestamp = LocalDateTime.now();
    }
    
    public StatusHistory(Shipment shipment, ShipmentStatus oldStatus, ShipmentStatus newStatus, String location) {
        this.shipment = shipment;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.location = location;
        this.timestamp = LocalDateTime.now();
    }
}

package com.hcl.parceltracking.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA Entity representing a historical status entry for a parcel.
 * 
 * This entity maintains a complete audit trail of all status changes
 * for a parcel throughout its journey.
 * 
 * @author HCL Technologies
 * @version 1.0.0
 */
@Entity
@Table(name = "status_history")
@Getter
@Setter
@ToString(exclude = "parcel")
@NoArgsConstructor
@AllArgsConstructor
public class StatusHistory {

    /**
     * Unique identifier for the status history entry
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Reference to the parent parcel
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parcel_id", nullable = false)
    private Parcel parcel;

    /**
     * Status of the parcel at this point in time
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ParcelStatus status;

    /**
     * Location where the status change occurred
     */
    @Column(nullable = false)
    private String location;

    /**
     * Timestamp when the status change occurred
     */
    @Column(nullable = false)
    private LocalDateTime timestamp;

    /**
     * Optional notes or comments about the status change
     */
    @Column(length = 500)
    private String notes;

    /**
     * JPA callback method to set timestamp before persisting
     */
    @PrePersist
    protected void onCreate() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }
}

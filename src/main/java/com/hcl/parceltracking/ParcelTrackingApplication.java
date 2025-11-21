package com.hcl.parceltracking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main application class for Parcel Tracking System.
 * 
 * This Spring Boot application provides REST APIs for:
 * - Customer parcel tracking
 * - Admin shipment management
 * - Automated notifications
 * 
 * @author Parcel Tracking Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableCaching
@EnableAsync
public class ParcelTrackingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParcelTrackingApplication.class, args);
    }
}

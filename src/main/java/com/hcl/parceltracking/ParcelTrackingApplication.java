package com.hcl.parceltracking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Main application class for the Parcel Tracking Service.
 * 
 * This microservice provides REST APIs for tracking parcels and managing shipments.
 * It implements enterprise-grade security, caching, and observability features.
 * 
 * @author HCL Technologies
 * @version 1.0.0
 */
@SpringBootApplication
@EnableCaching
public class ParcelTrackingApplication {

    /**
     * Main entry point for the Spring Boot application.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(ParcelTrackingApplication.class, args);
    }

}

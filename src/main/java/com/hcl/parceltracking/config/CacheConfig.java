package com.hcl.parceltracking.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Cache configuration for the Parcel Tracking System.
 * 
 * Configures Caffeine as the local cache provider with:
 * - 5-minute TTL for tracking data
 * - Maximum 1000 entries
 * - Statistics recording for monitoring
 */
@Configuration
@EnableCaching
public class CacheConfig {
    
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("trackingCache");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES) // Cache for 5 minutes
                .maximumSize(1000) // Max 1000 entries
                .recordStats()); // Enable statistics
        return cacheManager;
    }
}

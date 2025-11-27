package com.hcl.parceltracking.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Configuration for caching using Caffeine.
 * 
 * Defines cache managers and cache policies for the application.
 * 
 * @author HCL Technologies
 * @version 1.0.0
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Configure Caffeine cache manager.
     * 
     * @return cache manager with Caffeine implementation
     */
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("parcels", "trackingData");
        cacheManager.setCaffeine(caffeineCacheBuilder());
        return cacheManager;
    }

    /**
     * Configure Caffeine cache builder with eviction policies.
     * 
     * @return Caffeine cache builder
     */
    private Caffeine<Object, Object> caffeineCacheBuilder() {
        return Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)  // Cache expires after 10 minutes
                .maximumSize(1000)                        // Maximum 1000 entries in cache
                .recordStats();                           // Enable statistics for monitoring
    }
}

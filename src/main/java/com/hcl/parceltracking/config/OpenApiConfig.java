package com.hcl.parceltracking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration for OpenAPI documentation (Swagger).
 * 
 * Defines API metadata and server information for documentation.
 * 
 * @author HCL Technologies
 * @version 1.0.0
 */
@Configuration
public class OpenApiConfig {

    /**
     * Configure OpenAPI documentation.
     * 
     * @return OpenAPI configuration bean
     */
    @Bean
    public OpenAPI parcelTrackingOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Parcel Tracking System API")
                        .description("REST API for tracking parcels and managing shipments. " +
                                   "Provides real-time status updates and complete delivery history.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("HCL Technologies")
                                .email("support@hcl.com")
                                .url("https://www.hcl.com"))
                        .license(new License()
                                .name("Proprietary")
                                .url("https://www.hcl.com/license")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Development Server"),
                        new Server()
                                .url("https://api.parceltracking.com")
                                .description("Production Server")
                ));
    }
}

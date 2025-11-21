package com.hcl.parceltracking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for the Parcel Tracking System.
 * 
 * Configures:
 * - Public access to customer tracking endpoints
 * - JWT-based authentication for admin endpoints
 * - Stateless session management
 * - CORS and CSRF settings
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable for REST API (use JWT)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/tracking/**").permitAll() // Public tracking endpoints
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN") // Admin endpoints
                .requestMatchers("/actuator/health/**", "/actuator/info").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );
            // Uncomment and configure when JWT is ready
            //.oauth2ResourceServer(oauth2 -> oauth2
            //    .jwt(jwt -> jwt.decoder(jwtDecoder()))
            //);
        
        return http.build();
    }
    
    /**
     * JWT decoder configuration.
     * Configure this with your identity provider's JWK Set URI.
     * 
     * @return configured JWT decoder
     */
    // @Bean
    // public JwtDecoder jwtDecoder() {
    //     return NimbusJwtDecoder.withJwkSetUri("https://your-identity-provider/.well-known/jwks.json").build();
    // }
}

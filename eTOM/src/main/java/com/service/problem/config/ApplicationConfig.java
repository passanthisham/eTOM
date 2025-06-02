package com.service.problem.config;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/api")
public class ApplicationConfig extends ResourceConfig {
    public ApplicationConfig() {
        packages("com.service.problem.resources");
        register(org.glassfish.jersey.jackson.JacksonFeature.class);
        register(JacksonConfig.class);
    }
} 
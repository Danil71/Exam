package com.software.software_development.core.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "com.software.software-development")
public class AppConfigurationProperties {
    private Jwt jwt = new Jwt();
    private Admin admin = new Admin();

    @Data
    public static class Jwt {
        private String secretKey;
    }

    @Data
    public static class Admin {
        private String email;
        private String password;
        private String number;
    }
}

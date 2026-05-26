package com.software.software_development.core.security.configuration;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.software.software_development.core.configuration.Constants;
import com.software.software_development.core.security.enums.ApiPathExclusion;
import com.software.software_development.core.security.filter.JwtAuthenticationFilter;

import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(Arrays.stream(ApiPathExclusion.values())
                                .map(ApiPathExclusion::getPath)
                                .toArray(String[]::new)).permitAll()
                        .requestMatchers(HttpMethod.POST, Constants.API_URL + Constants.AUTH_URL + Constants.LOGIN_URL).permitAll()
                        .requestMatchers(HttpMethod.POST, Constants.API_URL + Constants.AUTH_URL + Constants.REGISTER_URL).permitAll()
                        .requestMatchers(HttpMethod.PUT, Constants.API_URL + Constants.AUTH_URL + "/refresh-token").permitAll()
                        .requestMatchers(HttpMethod.PUT, Constants.API_URL + Constants.AUTH_URL + "/refresh-token-direct").permitAll()
                        .requestMatchers(Constants.API_URL + Constants.ADMIN_PREFIX + "/**").hasRole("MANAGER")
                        .requestMatchers(Constants.API_URL + Constants.PRODUCTS_URL + "/**").authenticated()
                        .requestMatchers(Constants.API_URL + Constants.REVIEWS_URL + "/**").authenticated()
                        .requestMatchers(Constants.API_URL + Constants.PURCHASES_URL + "/**").authenticated()
                        .requestMatchers(Constants.API_URL + Constants.CATEGORIES_URL + "/**").authenticated()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

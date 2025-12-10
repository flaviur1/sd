package com.example.device_microservice.configs;

import com.example.device_microservice.filters.TokenValidationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final TokenValidationFilter tokenValidationFilter;

    public SecurityConfig(TokenValidationFilter tokenValidationFilter) {
        this.tokenValidationFilter = tokenValidationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/devices/getFor/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/devices/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/devices/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/devices/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/devices/**").authenticated()
                        .requestMatchers("/device-user/addByForm").permitAll()
                        .requestMatchers(HttpMethod.GET, "/device-user/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/device-user/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/device-user/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/device-user/**").authenticated()
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(tokenValidationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
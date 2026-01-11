package com.example.device_microservice.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class TokenValidationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        // Read user info from headers set by Traefik ForwardAuth
        String userIdHeader = request.getHeader("UserId");
        String rolesHeader = request.getHeader("UserRoles");

        if (userIdHeader == null || userIdHeader.isEmpty()) {
            // No auth headers - let Spring Security handle the request (might be permitAll)
            filterChain.doFilter(request, response);
            return;
        }

        try {
            UUID userId = UUID.fromString(userIdHeader);

            List<SimpleGrantedAuthority> authorities = (rolesHeader == null || rolesHeader.isEmpty())
                    ? Collections.emptyList()
                    : Arrays.stream(rolesHeader.split(","))
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null,
                    authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            // Invalid header format - continue without authentication
            filterChain.doFilter(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
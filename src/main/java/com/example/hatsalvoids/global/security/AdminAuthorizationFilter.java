package com.example.hatsalvoids.global.security;

import com.example.hatsalvoids.global.error.core.ErrorResponse;
import com.example.hatsalvoids.global.error.handler.GlobalErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Profile("prod")
@RequiredArgsConstructor
public class AdminAuthorizationFilter extends OncePerRequestFilter {

    private static final List<String> PROTECTED_PATH_PREFIXES = List.of(
            "/api/v1/management",
            "/actuator"
    );

    private final TokenClaimExtractor tokenClaimExtractor;
    private final ObjectMapper objectMapper;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        return PROTECTED_PATH_PREFIXES.stream().noneMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null || authorization.isBlank()) {
            writeUnauthorized(response, GlobalErrorCode.MISSING_HEADER);
            return;
        }
        String role = tokenClaimExtractor.extractRole(authorization).orElse(null);
        if (!"ADMIN".equals(role)) {
            writeUnauthorized(response, GlobalErrorCode.UNAUTHORIZED);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private void writeUnauthorized(HttpServletResponse response, GlobalErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(ErrorResponse.of(errorCode)));
    }
}

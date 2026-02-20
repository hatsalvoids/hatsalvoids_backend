package com.example.hatsalvoids.global.logging;

import com.example.hatsalvoids.global.security.RequestIdentity;
import com.example.hatsalvoids.global.security.RequestIdentityResolver;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class AccessLogFilter extends OncePerRequestFilter {

    private final RequestIdentityResolver requestIdentityResolver;

    public AccessLogFilter(RequestIdentityResolver requestIdentityResolver) {
        this.requestIdentityResolver = requestIdentityResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        long start = System.currentTimeMillis();
        try {
            filterChain.doFilter(request, response);
        } finally {
            long durationMs = System.currentTimeMillis() - start;
            RequestIdentity identity = requestIdentityResolver.resolveIdentity(request);
            log.info(
                    "access_log method={} uri={} query={} status={} durationMs={} clientIp={} xForwardedFor={} xRealIp={} userAgent={} referer={} origin={} subject={} userId={} email={} role={} tokenHash={}",
                    request.getMethod(),
                    request.getRequestURI(),
                    request.getQueryString(),
                    response.getStatus(),
                    durationMs,
                    identity.clientIp(),
                    identity.forwardedFor(),
                    identity.realIp(),
                    identity.userAgent(),
                    identity.referer(),
                    identity.origin(),
                    identity.subject(),
                    identity.userId(),
                    identity.email(),
                    identity.role(),
                    identity.tokenHash()
            );
        }
    }
}

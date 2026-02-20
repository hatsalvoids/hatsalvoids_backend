package com.example.hatsalvoids.shade;

import com.example.hatsalvoids.global.config.ShadeRateLimitProperties;
import com.example.hatsalvoids.global.security.RequestIdentityResolver;
import com.example.hatsalvoids.shade.common.ShadeErrorCode;
import com.example.hatsalvoids.shade.common.ShadeException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class ShadeRateLimitService {

    private final ShadeRateLimitProperties rateLimitProperties;
    private final RequestIdentityResolver requestIdentityResolver;
    private final Map<String, RequestWindow> requestWindows = new ConcurrentHashMap<>();

    public void validateShadeRequest(HttpServletRequest request) {
        String identifier = requestIdentityResolver.resolveIdentifier(request);
        RequestWindow window = requestWindows.computeIfAbsent(identifier, key -> new RequestWindow());
        if (!window.tryAcquire(rateLimitProperties.maxRequests(), rateLimitProperties.windowSeconds())) {
            throw new ShadeException(ShadeErrorCode.TOO_MANY_REQUESTS);
        }
    }

    private static class RequestWindow {
        private final Deque<Instant> requests = new ArrayDeque<>();

        public synchronized boolean tryAcquire(int maxRequests, long windowSeconds) {
            Instant now = Instant.now();
            Instant cutoff = now.minus(Duration.ofSeconds(windowSeconds));
            while (!requests.isEmpty() && requests.peekFirst().isBefore(cutoff)) {
                requests.pollFirst();
            }
            if (requests.size() >= maxRequests) {
                return false;
            }
            requests.addLast(now);
            return true;
        }
    }
}

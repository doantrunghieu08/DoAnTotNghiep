package com.TrungHieu.DoAnTotNgiep.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Filter giới hạn request dựa trên IP của client.
 * Sử dụng Redis (Fixed Window Counter) để đếm số request.
 * Giới hạn mặc định: 100 requests / 1 phút / IP.
 */
@Component
@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {

    private static final int MAX_REQUESTS_PER_MINUTE = 100;
    private static final long WINDOW_SECONDS = 60L;

    private final StringRedisTemplate redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String ip = getClientIP(request);
        String key = "rate_limit:" + ip;

        try {
            // Tăng biến đếm trong Redis
            Long requestCount = redisTemplate.opsForValue().increment(key);

            // Nếu là request đầu tiên trong window, set TTL 60 giây
            if (requestCount != null && requestCount == 1) {
                redisTemplate.expire(key, Duration.ofSeconds(WINDOW_SECONDS));
            }

            if (requestCount != null && requestCount <= MAX_REQUESTS_PER_MINUTE) {
                long remaining = MAX_REQUESTS_PER_MINUTE - requestCount;
                response.setHeader("X-Rate-Limit-Limit", String.valueOf(MAX_REQUESTS_PER_MINUTE));
                response.setHeader("X-Rate-Limit-Remaining", String.valueOf(remaining));
                filterChain.doFilter(request, response);
            } else {
                // Lấy thời gian còn lại của window hiện tại
                Long ttl = redisTemplate.getExpire(key, TimeUnit.SECONDS);
                long retryAfter = (ttl != null && ttl > 0) ? ttl : WINDOW_SECONDS;

                response.setHeader("X-Rate-Limit-Retry-After-Seconds", String.valueOf(retryAfter));
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(
                        "{\"status\": 429, \"error\": \"Too Many Requests\", " +
                        "\"message\": \"Bạn đã vượt quá " + MAX_REQUESTS_PER_MINUTE +
                        " request/phút. Vui lòng thử lại sau " + retryAfter + " giây.\"}"
                );
            }
        } catch (Exception e) {
            // Cơ chế an toàn (Fail-open): Nếu Redis sập, log lỗi và cho phép request đi qua
            System.err.println("[RateLimit] Redis is down or unreachable: " + e.getMessage());
            filterChain.doFilter(request, response);
        }
    }

    /**
     * Lấy IP thực của client, có hỗ trợ trường hợp đứng sau proxy/Nginx.
     */
    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null || xfHeader.isBlank()) {
            return request.getRemoteAddr();
        }
        // X-Forwarded-For có thể chứa nhiều IP (chain), lấy IP đầu tiên
        return xfHeader.split(",")[0].trim();
    }
}

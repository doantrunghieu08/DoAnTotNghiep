package com.TrungHieu.DoAnTotNgiep.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    JwtTokenProvider tokenProvider;
    UserDetailsService userDetailService;


    // Trong JwtAuthenticationFilter.java
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)){
                String username = tokenProvider.getUsernameFromJWT(jwt);
                UserDetails userDetails = userDetailService.loadUserByUsername(username);

                if(userDetails != null){
                    // QUAN TRỌNG: Truyền Role vào tham số thứ 3
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities() // <--- KHÔNG ĐƯỢC QUÊN CÁI NÀY
                    );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        catch (Exception exception){

            System.err.println("❌ LỖI TRONG FILTER: ");
            exception.printStackTrace(); // In chi tiết lỗi ra console
        }

        filterChain.doFilter(request,response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return path.contains("/login") || path.contains("/register") || path.contains("/public") || path.contains("/image");
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        // 1. Lấy danh sách toàn bộ Cookies từ request
        jakarta.servlet.http.Cookie[] cookies = request.getCookies();

        // 2. Kiểm tra nếu có Cookies
        if (cookies != null) {
            for (jakarta.servlet.http.Cookie cookie : cookies) {
                // 3. Tìm đúng cái tên "accessToken" mà mình đã đặt ở Controller
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        // Nếu không tìm thấy trong Cookie, sếp có thể để logic dự phòng lấy từ Header
        // (nhưng nếu đã chuyển sang HttpOnly 100% thì return null luôn là chuẩn bài)
        return null;
    }
}

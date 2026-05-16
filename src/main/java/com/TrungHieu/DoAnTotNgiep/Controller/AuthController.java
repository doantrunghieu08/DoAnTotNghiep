package com.TrungHieu.DoAnTotNgiep.Controller;

import com.TrungHieu.DoAnTotNgiep.DTO.Request.AuthRequest.*;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.ApiResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.UserResponse;
import com.TrungHieu.DoAnTotNgiep.Exception.AppException;
import com.TrungHieu.DoAnTotNgiep.Exception.ErrorCode;
import com.TrungHieu.DoAnTotNgiep.Service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    AuthService authService;

    @PostMapping("admin/api/user/create")
    public ResponseEntity<ApiResponse<UserResponse>> addUser(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(authService.register(request));
    }

    @GetMapping("/admin/api/user")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUserList(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(authService.getUserList(page, size));
    }

    @PostMapping("/public/api/auth/login")
    public ResponseEntity<ApiResponse<UserResponse>> login(@RequestBody LoginRequest request) {

        ApiResponse<UserResponse> apiResponse = authService.login(request);


        if (apiResponse.getCode() == 202) {
            return ResponseEntity.status(202).body(apiResponse);
        }

        // 3. Lấy dữ liệu UserResponse bên trong ra để xử lý Token
        UserResponse userResponse = apiResponse.getData();

        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", userResponse.getToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(43200)
                .sameSite("Lax")
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", userResponse.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(604800)
                .sameSite("Lax")
                .build();


        userResponse.setToken(null);
        userResponse.setRefreshToken(null);

        // 7. Trả về ResponseEntity kèm Header Set-Cookie
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        headers.add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        return ResponseEntity.ok()
                .headers(headers)
                .body(apiResponse);
    }

    @PostMapping("/public/api/auth/forgot-password")
    public ResponseEntity<ApiResponse<String>> fogotPassword(@RequestBody ForgotPasswordRequest request){
        String email = request.getEmail();
        return ResponseEntity.ok(authService.forgotPassword(email));
    }

    @PostMapping("/public/api/auth/change-password")
    public ResponseEntity<ApiResponse<UserResponse>> changePassword(@RequestBody ChangePasswordRequest request){
        return ResponseEntity.ok(authService.changePassword(request));
    }

    @PostMapping("/public/api/auth/refresh")
    public ResponseEntity<ApiResponse<Void>> refresh(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        if (refreshToken == null) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        // Lấy access token mới từ service
        ApiResponse<String> result = authService.refreshAccessToken(refreshToken);
        String newAccessToken = result.getData();

        // Set accessToken vào HttpOnly cookie — JS không đọc được (chống XSS)
        ResponseCookie accessCookie = ResponseCookie.from("accessToken", newAccessToken)
                .httpOnly(true)
                .secure(false)       // đổi thành true khi deploy HTTPS
                .path("/")
                .maxAge(43200)        // khớp với maxAge lúc login
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .body(new ApiResponse<>(200, "Cấp lại Token thành công", null));
    }

    @PostMapping("/public/api/auth/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @CookieValue(name = "refreshToken", required = false) String refreshToken) {
        authService.logout(refreshToken);
        return ResponseEntity.ok(new ApiResponse<>(200, "Đăng xuất thành công", null));
    }


}

package com.TrungHieu.DoAnTotNgiep.Service;

import com.TrungHieu.DoAnTotNgiep.DTO.Request.AuthRequest.ChangePasswordRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Request.AuthRequest.LoginRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Request.AuthRequest.RegisterRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.ApiResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.UserResponse;
import com.TrungHieu.DoAnTotNgiep.Entity.Classes;
import com.TrungHieu.DoAnTotNgiep.Entity.RefreshToken;
import com.TrungHieu.DoAnTotNgiep.Entity.User;
import com.TrungHieu.DoAnTotNgiep.Enum.RoleType;
import com.TrungHieu.DoAnTotNgiep.Exception.AppException;
import com.TrungHieu.DoAnTotNgiep.Exception.ErrorCode;
import com.TrungHieu.DoAnTotNgiep.Mapper.UserMapper;
import com.TrungHieu.DoAnTotNgiep.Repository.ClassRepository;
import com.TrungHieu.DoAnTotNgiep.Repository.RefreshTokenRepository;
import com.TrungHieu.DoAnTotNgiep.Repository.UserRepository;
import com.TrungHieu.DoAnTotNgiep.Security.JwtTokenProvider;
import com.cloudinary.Api;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.RandomStringUtils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {
    UserRepository userRepository;
    ClassRepository classRepository;
    UserMapper userMapper;
    PasswordEncoder encoder;
    AuthenticationManager manager;
    JwtTokenProvider jwtTokenProvider;
    EmailService emailService;
    RefreshTokenRepository refreshTokenRepository;

    public ApiResponse<UserResponse> register(RegisterRequest request){
        User user = new User();
        user.setId(request.getId());
        user.setFullName(request.getFullName());
        user.setUsername(request.getUsername().trim());
        String passwordHash = encoder.encode(request.getPassword());
        user.setPassword(passwordHash);
//        if (RoleType.STUDENT.equals(request.getRole()) && request.getClassID() != null) {
//            if (request.getClassID() == null || request.getClassID().trim().isEmpty()) {
//                throw new AppException(ErrorCode.CLASS_REQUIRED_FOR_STUDENT);
//
//            }
//            Classes studentClass = classRepository.findById(request.getClassID())
//                    .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));
//            user.setStudentClass(studentClass);
//        }
        user.setIsRequirePasswordChange(false);
        user.setRole(request.getRole());


        user.setBirthday(request.getBirthday());
        user.setEmail(request.getEmail());
        user.setGender(request.getGender());
        user.setStatus(request.getStatusType());
        user.setAvatarImage(request.getAvatarImage());
        user.setIsRequirePasswordChange(false);
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);
        UserResponse userResponse = userMapper.toResponse(user);
        return new ApiResponse<>(201, "Dang ky thanh cong", userResponse);

    }


    public ApiResponse<UserResponse> login(LoginRequest request){

            var auth = manager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));

            UserResponse userResponse = userMapper.toResponse(user);

            if (user.getIsRequirePasswordChange()){
                userResponse.setRequirePasswordChange(true);
                return new ApiResponse<>(204, "Vui lòng đổi mật khẩu mới để tiếp tục", userResponse);
            }


            String token = jwtTokenProvider.generateToken(auth);
            userResponse.setToken(token);


            String refreshTokenStr = createRefreshToken(user);
            userResponse.setRefreshToken(refreshTokenStr);

            return new ApiResponse<>(200, "Dang nhap thanh cong", userResponse);


    }

    public ApiResponse<String> forgotPassword(String email) {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Email không tồn tại!"));

            String newPassword = RandomStringUtils.randomAlphanumeric(8);
            user.setPassword(encoder.encode(newPassword));
            user.setIsRequirePasswordChange(true);
            userRepository.save(user);


            emailService.sendForgotPasswordEmail(email, user.getUsername(), newPassword);

            return new ApiResponse<>(200, "Đã gửi mật khẩu mới vào email của bạn.", null);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public ApiResponse<UserResponse> changePassword(ChangePasswordRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));


        if (encoder.matches(request.getOldPassword(), user.getPassword())) {


            user.setPassword(encoder.encode(request.getNewPassword()));


            user.setIsRequirePasswordChange(false);


            userRepository.save(user);

        } else {

            throw new AppException(ErrorCode.PASSWORD_INCORRECT);
        }

        UserResponse userResponse = userMapper.toResponse(user);
        return new ApiResponse<>(200, "Đổi mật khẩu thành công", userResponse);
    }


    public ApiResponse<List<UserResponse>> getUserList(int page, int size){
        Pageable pageable = PageRequest.of(page, size);

        Page<User> userPage = userRepository.findAll(pageable);

        List<UserResponse> userResponseList = userPage.map(userMapper::toResponse).getContent();

        return new ApiResponse<>(200, "Danh sách người dùng", userResponseList);
    }

    public String createRefreshToken(User user) {
        refreshTokenRepository.deleteByUser(user);

        String token = UUID.randomUUID().toString();
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(token)
                .expiryDate(Instant.now().plus(7, ChronoUnit.DAYS))
                .build();

        refreshTokenRepository.save(refreshToken);
        return token;
    }
    public ApiResponse<String> refreshAccessToken(String requestToken) {
        return refreshTokenRepository.findByToken(requestToken)
                .map(token -> {
                    // Kiểm tra hết hạn
                    if (token.getExpiryDate().isBefore(Instant.now())) {
                        refreshTokenRepository.delete(token);
                        throw new AppException(ErrorCode.UNAUTHENTICATED);
                    }
                    // Tạo Access Token mới (Sếp dùng hàm sinh JWT cũ của sếp)
                    String newAccessToken = jwtTokenProvider.generateTokenFromUser(token.getUser());
                    return new ApiResponse<>(200, "Cấp lại Token thành công", newAccessToken);
                })
                .orElseThrow(() -> new AppException(ErrorCode.TOKEN_INVALID));
    }

    @Transactional
    public void logout(String refreshTokenStr) {
        refreshTokenRepository.findByToken(refreshTokenStr)
                .ifPresent(token -> refreshTokenRepository.delete(token));

    }


}

package com.TrungHieu.DoAnTotNgiep.Service;

import com.TrungHieu.DoAnTotNgiep.DTO.Request.AuthRequest.SearchingStudentRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Request.UserRequest.AddiationStudentToTheClassRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Request.UserRequest.RemoveStudentFromClassRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.StudentResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Request.UserRequest.UpdateUserRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.ApiResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.UserDashboardResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.UserResponse;
import com.TrungHieu.DoAnTotNgiep.Entity.Classes;
import com.TrungHieu.DoAnTotNgiep.Entity.User;
import com.TrungHieu.DoAnTotNgiep.Enum.RoleType;
import com.TrungHieu.DoAnTotNgiep.Enum.UserStatusType;
import com.TrungHieu.DoAnTotNgiep.Exception.AppException;
import com.TrungHieu.DoAnTotNgiep.Exception.ErrorCode;
import com.TrungHieu.DoAnTotNgiep.Mapper.UserMapper;
import com.TrungHieu.DoAnTotNgiep.Repository.ClassRepository;
import com.TrungHieu.DoAnTotNgiep.Repository.UserRepository;
import com.beust.ah.A;
import com.cloudinary.Api;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    ClassRepository classRepository;
    UserMapper userMapper;
    PasswordEncoder encoder;


    public ApiResponse<UserResponse> updateUser(String id, UpdateUserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));

        // 1. Kiểm tra Username (Chỉ check và đổi nếu request gửi lên khác với hiện tại)
        if (request.getUsername() != null && !request.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new AppException(ErrorCode.DATA_ALREADY_EXISTS);
            }
            user.setUsername(request.getUsername());
        }


        // 3. Sử dụng helper để chỉ set những trường có thay đổi và không null
        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getRole() != null) user.setRole(request.getRole());
        if (request.getBirthday() != null) user.setBirthday(request.getBirthday());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getGender() != null) user.setGender(request.getGender());
        if (request.getStatusType() != null) user.setStatus(request.getStatusType());
        if (request.getAvatarImage() != null) user.setAvatarImage(request.getAvatarImage());

        userRepository.save(user);
        return new ApiResponse<>(200, "Cập nhật thành công", userMapper.toResponse(user));
    }

    public ApiResponse<UserResponse> LockedUser(String id){
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));
        user.setStatus(UserStatusType.LOCKED);
        userRepository.save(user);
        UserResponse userResponse = userMapper.toResponse(user);
        return new ApiResponse<>(200, "Khóa tài khoản thành công", userResponse);
    }

    public ApiResponse<UserResponse> unlockUser(String id){
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));
        user.setStatus(UserStatusType.ACTIVE);
        userRepository.save(user);
        UserResponse userResponse = userMapper.toResponse(user);
        return new ApiResponse<>(200, "Mở tài khoản thành công", userResponse);
    }

    public ApiResponse<StudentResponse> searchStudent(SearchingStudentRequest request){
        User user = userRepository.findById(request.getId()).orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));
        StudentResponse studentResponse = userMapper.toStudentResponse(user);
        return new ApiResponse<>(200, "Tìm kiếm thành công", studentResponse);
    }

    @Transactional
    public ApiResponse<StudentResponse> addStudentToTheClass(AddiationStudentToTheClassRequest request) {

        User user = userRepository.findById(request.getIdStudent())
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));


        Classes classes = classRepository.findById(request.getIdClass())
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));

        if (!RoleType.STUDENT.equals(user.getRole())) {
            throw new AppException(ErrorCode.INVALID_ROLE);
        }

        if (user.getStudentClass() != null) {
            throw new AppException(ErrorCode.DATA_ALREADY_EXISTS);
        }

        user.setStudentClass(classes);
        userRepository.save(user);

        StudentResponse studentResponse = userMapper.toStudentResponse(user);
        return new ApiResponse<>(200, "Thêm sinh viên vào lớp thành công", studentResponse);
    }

    public ApiResponse<Page<StudentResponse>> findStudentByClassId(String classId, int page, int size) {
        // 1. Tạo Pageable (Sắp xếp theo tên sinh viên từ A-Z)
        Pageable pageable = PageRequest.of(page, size, Sort.by("fullName").ascending());

        // 2. Lấy dữ liệu phân trang
        Page<StudentResponse> studentPage = userRepository.findStudentsByClassId(classId, pageable);

        // 3. Trả về kết quả (Vì đã là DTO sẵn nên không cần map lại)
        return new ApiResponse<>(200, "Lấy danh sách sinh viên theo lớp thành công", studentPage);
    }

    public ApiResponse<UserResponse> getInfo(String username){
        User user = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));
        return new ApiResponse<>(200, "Lấy thông tin thành công", userMapper.toResponse(user));
    }

    public ApiResponse<StudentResponse> removeStudentFromClass(RemoveStudentFromClassRequest request){
        User user = userRepository.findStudentByClassIdAndId(request.getIdClass(), request.getIdStudent()).orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));
        user.setStudentClass(null);
        userRepository.save(user);
        return new ApiResponse<>(200, "Xóa sinh viên khỏi lớp thành công", userMapper.toStudentResponse(user));
    }

    public ApiResponse<List<UserResponse>> getUserByRole(String roleType, int page, int size){
        Pageable pageable = PageRequest.of(page, size);

        // 1. Lọc và phân trang trực tiếp từ Database
        Page<User> userPage = userRepository.findByRoleNative(roleType, pageable);

        // 2. Map sang DTO và lấy danh sách của trang hiện tại
        List<UserResponse> userResponseList = userPage.map(userMapper::toResponse).getContent();

        return new ApiResponse<>(200, "Danh sách user theo vai trò: " + roleType, userResponseList);
    }

    public ApiResponse<List<UserResponse>> getTeacherList(){
        List<UserResponse> userResponseList = userMapper.toResponseList(userRepository.findAllByRole(RoleType.TEACHER));
        return new ApiResponse<>(200, "Danh sách giáo viên", userResponseList);
    }

    public ApiResponse<Page<UserResponse>> globalSearch(String keyword, int page, int size) {
        // Sắp xếp theo tên từ A-Z
        Pageable pageable = PageRequest.of(page, size, Sort.by("fullName").ascending());

        // Gọi hàm tìm kiếm không lọc Role
        Page<User> userPage = userRepository.searchAllUsers(keyword, pageable);

        return new ApiResponse<>(200, "Kết quả tìm kiếm cho: " + keyword,
                userPage.map(userMapper::toResponse));
    }

    public ApiResponse<Page<UserResponse>> searchStudentsInClass(String classId, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("username").ascending());

        Page<User> studentPage = userRepository.searchStudentsInClass(classId, keyword, pageable);

        return new ApiResponse<>(200, "Danh sách tìm kiếm sinh viên lớp " + classId,
                studentPage.map(userMapper::toResponse));
    }

    public ApiResponse<UserDashboardResponse> getUserStatistics() {
        // 1. Đếm tổng số người dùng
        long total = userRepository.count();

        // 2. Đếm theo Role (Sếp dùng Enum RoleType đã có)
        long teachers = userRepository.countByRole(RoleType.TEACHER);
        long students = userRepository.countByRole(RoleType.STUDENT);

        // 3. Đếm số tài khoản bị khóa (Dùng Enum UserStatusType)
        long locked = userRepository.countByStatus(UserStatusType.LOCKED);

        UserDashboardResponse stats = UserDashboardResponse.builder()
                .totalUsers(total)
                .totalTeachers(teachers)
                .totalStudents(students)
                .lockedUsers(locked)
                .build();

        return new ApiResponse<>(200, "Lấy thống kê thành công", stats);
    }

    public ApiResponse<UserResponse> uploadAvatar(String id,String avatarImage){
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));
        user.setAvatarImage(avatarImage);
        userRepository.save(user);
        return new ApiResponse<>(200, "Upload avatar thanh cong", userMapper.toResponse(user));
    }




}

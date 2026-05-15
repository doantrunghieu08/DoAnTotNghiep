package com.TrungHieu.DoAnTotNgiep.Controller;

import com.TrungHieu.DoAnTotNgiep.DTO.Request.AuthRequest.SearchingStudentRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Request.UserRequest.AddiationStudentToTheClassRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Request.UserRequest.RemoveStudentFromClassRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.StudentResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Request.UserRequest.UpdateUserRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.ApiResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.UserDashboardResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.UserResponse;
import com.TrungHieu.DoAnTotNgiep.Service.ExcelService;
import com.TrungHieu.DoAnTotNgiep.Service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;
    ExcelService excelService;

    @PutMapping("/admin/api/user/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable String id, @RequestBody UpdateUserRequest request){
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @PutMapping("/admin/api/lock-user/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> lockedUser(@PathVariable String id){
        return ResponseEntity.ok(userService.LockedUser(id));
    }

    @PutMapping("/admin/api/unlock-user/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> unlockUser(@PathVariable String id){
        return ResponseEntity.ok(userService.unlockUser(id));
    }

    @PostMapping("/admin/api/student")
    public ResponseEntity<ApiResponse<StudentResponse>> searchStudent(@RequestBody SearchingStudentRequest request){
        return ResponseEntity.ok(userService.searchStudent(request));
    }

    @PostMapping("/admin/api/class/student")
    public ResponseEntity<ApiResponse<StudentResponse>> addStudentToTheClass(@RequestBody AddiationStudentToTheClassRequest request){
        return ResponseEntity.ok(userService.addStudentToTheClass(request));
    }

    @GetMapping("/classes/api/{id}")
    public ResponseEntity<ApiResponse<Page<StudentResponse>>> getStudentsByClassId(@PathVariable String id, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size){
        return ResponseEntity.ok(userService.findStudentByClassId(id, page, size));
    }

    @GetMapping("/public/api/auth/me/{username}")
    public ResponseEntity<ApiResponse<UserResponse>> getInfo(@PathVariable String username){
        return ResponseEntity.ok(userService.getInfo(username));
    }

    @PutMapping("/admin/api/class/user")
    public ResponseEntity<ApiResponse<StudentResponse>> updateStudent(@RequestBody RemoveStudentFromClassRequest request){
        return ResponseEntity.ok(userService.removeStudentFromClass(request));
    }

    @PostMapping(value = "/admin/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Object>> importExcel(@RequestParam("file") MultipartFile file) {

        // 1. Kiểm tra file trống
        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(400, "File trống sếp ơi!", null));
        }

        // 2. Xử lý logic qua Service
        List<String> errors = excelService.importUsersFromExcel(file);

        // 3. Phản hồi dựa trên kết quả
        if (!errors.isEmpty()) {
            // Trả về lỗi 400 kèm danh sách lỗi chi tiết
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(400, "Dữ liệu Excel không hợp lệ", errors));
        }

        // Trả về 200 OK khi mọi thứ mượt mà
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Import thành công: " + file.getOriginalFilename(), null)
        );
    }

    @GetMapping("/admin/filter/user")
    public ResponseEntity<ApiResponse> getStudentsByRole(@RequestParam(defaultValue = "ADMIN") String role, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(userService.getUserByRole(role,page,size));
    }

    @GetMapping("/admin/teacher-list")
    public ResponseEntity<ApiResponse<List<UserResponse>>>  getTeacherList(){
        return ResponseEntity.ok(userService.getTeacherList());
    }

    @GetMapping("/admin/global-search")
    public ApiResponse<Page<UserResponse>> globalSearch(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return userService.globalSearch(keyword, page, size);
    }

    // Endpoint tìm kiếm sinh viên theo lớp
    @GetMapping("/public/classes/{classId}/search")
    public ApiResponse<Page<UserResponse>> searchInClass(
            @PathVariable String classId,
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return userService.searchStudentsInClass(classId, keyword, page, size);
    }

    @GetMapping("/public/statistics")
    public ResponseEntity<ApiResponse<UserDashboardResponse>> getStats() {
        return ResponseEntity.ok(userService.getUserStatistics());
    }

    @PostMapping("/public/upload/avatar/{idUser}")
    public ResponseEntity<ApiResponse<UserResponse>> uploadAvatar(@PathVariable String idUser, @RequestBody String avatarImage){
        return ResponseEntity.ok(userService.uploadAvatar(idUser, avatarImage));
    }


}

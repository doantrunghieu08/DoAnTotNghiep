package com.TrungHieu.DoAnTotNgiep.Controller;

import com.TrungHieu.DoAnTotNgiep.DTO.Request.ClassRequest.CreationClassRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Request.ClassRequest.TeachingAssignmentRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Request.ClassRequest.UpdationClassRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.ApiResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.ClassResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.UserResponse;
import com.TrungHieu.DoAnTotNgiep.Service.ClassService;
import com.TrungHieu.DoAnTotNgiep.Service.ExcelService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClassController {
    ClassService classService;
    ExcelService excelService;

    @PostMapping("/admin/api/class")
    public ResponseEntity<ApiResponse<ClassResponse>> createClass(@RequestBody CreationClassRequest request){
        return ResponseEntity.ok(classService.createClass(request));
    }

    @GetMapping("/admin/api/class")
    public ResponseEntity<ApiResponse<Page<ClassResponse>>> getClassList(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "8") int size){
        return ResponseEntity.ok(classService.getClassList(page,size));
    }

    @PutMapping("/admin/api/class")
    public ResponseEntity<ApiResponse<ClassResponse>> teachingAssignment(@RequestBody TeachingAssignmentRequest request){
        return ResponseEntity.ok(classService.teachingAssignment(request));
    }

    @PutMapping("/admin/api/class/{id}")
    public ResponseEntity<ApiResponse<ClassResponse>> updateClass(@PathVariable String id, @RequestBody UpdationClassRequest request){
        return ResponseEntity.ok(classService.updateClass(id, request));
    }

    @GetMapping("/classes/total-student")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getStudentCountPerClass(){
        return ResponseEntity.ok(classService.getStudentCountPerClass());
    }

    @PostMapping(value = "/admin/import-to-class/{classId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Object>> importStudentToClass(
            @PathVariable String classId,
            @RequestParam("file") MultipartFile file) {

        List<String> errors = classService.importStudentsToClass(file, classId);

        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, "Có lỗi xảy ra", errors));
        }
        return ResponseEntity.ok(new ApiResponse<>(200, "Thêm sinh viên vào lớp thành công!", null));
    }

    @PostMapping(value = "/admin/import-classes", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Object>> importClasses(@RequestParam("file") MultipartFile file) {
        List<String> errors = excelService.importClassesFromExcel(file);
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, "Lỗi dữ liệu file lớp học", errors));
        }
        return ResponseEntity.ok(new ApiResponse<>(200, "Import danh sách lớp thành công!", null));
    }

    @DeleteMapping("/admin/api/class/{id}")
    public ResponseEntity<ApiResponse<ClassResponse>> removeClass(@PathVariable String id){
        return ResponseEntity.ok(classService.removeClass(id));
    }

    @GetMapping("/teacher/api/class/{id}")
    public ResponseEntity<ApiResponse<Page<ClassResponse>>> getClassesByTeacher(@PathVariable String id, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "8")int size){
        return ResponseEntity.ok(classService.getClassesByTeacher(id, page, size));
    }


}

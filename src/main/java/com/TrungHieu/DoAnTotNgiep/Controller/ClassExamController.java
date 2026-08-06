package com.TrungHieu.DoAnTotNgiep.Controller;

import com.TrungHieu.DoAnTotNgiep.DTO.Request.ExamRequest.AddiationExamToClassRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.ApiResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.ClassExamResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.ExamResponse;
import com.TrungHieu.DoAnTotNgiep.Service.ClassExamService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClassExamController {
    ClassExamService classExamService;

    @PostMapping("/api/teacher/exam/class")
    public ResponseEntity<ApiResponse<ClassExamResponse>> addExamToClass(@RequestBody AddiationExamToClassRequest request){
        return ResponseEntity.ok(classExamService.addExamToClass(request));
    }

    @GetMapping("/public/exam/class/{idClass}")
    public ResponseEntity<ApiResponse<Page<ExamResponse>>> getExamByClassId(@PathVariable String idClass,
                                                                            @RequestParam(defaultValue = "0") int page,
                                                                            @RequestParam(defaultValue = "5") int size)
    {
        return ResponseEntity.ok(classExamService.getExamByClassId(idClass, page, size));
    }

    @DeleteMapping("/api/teacher/exam/class")
    public ResponseEntity<ApiResponse<String>> deletedExamFromClass(
            @RequestParam String idClass,
            @RequestParam int idExam) {
        classExamService.deletedExam(idClass, idExam);

        // Trả về 200 OK kèm Message để Frontend dễ xử lý
        return ResponseEntity.ok(new ApiResponse<>(200, "Xóa thành công bài thi khỏi lớp", null));
    }


    @GetMapping("/api/teacher/exam/class/deleted")
    public ResponseEntity<ApiResponse<Page<ExamResponse>>> getDeletedExams(
            @RequestParam String idClass,           // ← đổi thành @RequestParam
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(classExamService.getDeletedExamsByClassId(idClass, page, size));
    }

    @PostMapping("/api/teacher/restore")
    public ResponseEntity<ApiResponse<ClassExamResponse>> restoreExam(
            @RequestParam String idClass,
            @RequestParam int idExam) {

        return ResponseEntity.ok(classExamService.restoreExam(idClass, idExam));
    }


}

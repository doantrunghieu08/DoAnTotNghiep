package com.TrungHieu.DoAnTotNgiep.Controller;

import com.TrungHieu.DoAnTotNgiep.DTO.Request.ExamRequest.CreationExamRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.ApiResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.ExamTypeResponse;
import com.TrungHieu.DoAnTotNgiep.Service.ExamTypeService;
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
public class ExamTypeController {
    ExamTypeService examTypeService;

    @GetMapping("/api/exam/teacher/{idTeacher}")
    public ResponseEntity<ApiResponse<Page<ExamTypeResponse>>> getExamTypesByTeacherId(@PathVariable String idTeacher, @RequestParam(defaultValue = "0")int page, @RequestParam(defaultValue = "9") int size){
        return ResponseEntity.ok(examTypeService.getAllExamByTeacherId(idTeacher,page, size));
    }

    @PostMapping("/api/teacher/exam")
    public ResponseEntity<ApiResponse<ExamTypeResponse>> creationExam(@RequestBody CreationExamRequest request){
        return ResponseEntity.ok(examTypeService.creationExam(request));
    }

    @DeleteMapping("/api/teacher/exam/{id}")
    public ResponseEntity<ApiResponse<String>> deleteExam(@PathVariable int id){
        examTypeService.deleteExam(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Xóa bài thi thành công", null));
    }

    @PutMapping("/api/teacher/exam/{id}")
    public ResponseEntity<ApiResponse<String>> restoreExam(@PathVariable int id){
        examTypeService.restoreExam(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Khôi phục bài thi thành công", null));
    }


}

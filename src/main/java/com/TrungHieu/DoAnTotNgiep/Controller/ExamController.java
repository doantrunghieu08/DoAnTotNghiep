package com.TrungHieu.DoAnTotNgiep.Controller;

import com.TrungHieu.DoAnTotNgiep.DTO.Request.ExamRequest.CreationExamRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Request.ExamRequest.UpdationExamRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.ApiResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.ExamTypeResponse;
import com.TrungHieu.DoAnTotNgiep.Service.ExamService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExamController {
    ExamService examService;

    @GetMapping("/api/exam/teacher/{idTeacher}")
    public ResponseEntity<ApiResponse<Page<ExamTypeResponse>>> getExamTypesByTeacherId(@PathVariable String idTeacher, @RequestParam(defaultValue = "0")int page, @RequestParam(defaultValue = "9") int size){
        return ResponseEntity.ok(examService.getAllExamByTeacherId(idTeacher,page, size));
    }

    @PostMapping("/api/teacher/exam")
    public ResponseEntity<ApiResponse<ExamTypeResponse>> creationExam(@RequestBody CreationExamRequest request){
        return ResponseEntity.ok(examService.creationExam(request));
    }

    @DeleteMapping("/api/teacher/exam/{id}")
    public ResponseEntity<ApiResponse<String>> deleteExam(@PathVariable int id){
        examService.deleteExam(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Xóa bài thi thành công", null));
    }

    @PutMapping("/api/teacher/exam/{id}")
    public ResponseEntity<ApiResponse<String>> restoreExam(@PathVariable int id){
        examService.restoreExam(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Khôi phục bài thi thành công", null));
    }

    @PutMapping("/api/teacher/update-exam/{idExam}")
    public ResponseEntity<ApiResponse<ExamTypeResponse>> updateExam(@PathVariable int idExam,@RequestBody UpdationExamRequest request){
        ApiResponse<ExamTypeResponse> apiResponse = examService.updateExam(idExam, request);
        return ResponseEntity.ok(apiResponse);
    }




}

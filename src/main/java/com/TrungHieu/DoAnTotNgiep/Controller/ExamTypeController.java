package com.TrungHieu.DoAnTotNgiep.Controller;

import com.TrungHieu.DoAnTotNgiep.DTO.Request.ExamTypeRequest.CreationExamTypeRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Request.ExamTypeRequest.UpdationExamCodeRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.ApiResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.ExamCodeResponse;
import com.TrungHieu.DoAnTotNgiep.Entity.ExamType;
import com.TrungHieu.DoAnTotNgiep.Service.ExamTypeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExamTypeController {
    ExamTypeService examTypeService;

    @PostMapping("/api/admin/exam-type")
    public ResponseEntity<ApiResponse<ExamCodeResponse>> creationRequest(@RequestBody CreationExamTypeRequest request){
        return ResponseEntity.ok(examTypeService.creationExamType(request));
    }

    @GetMapping("/api/teacher/exam-type")
    public ResponseEntity<ApiResponse<List<ExamCodeResponse>>> getExamType(){
        return ResponseEntity.ok(examTypeService.getExamTypes());
    }
    @GetMapping("/api/admin/exam-type/exam")
    public ResponseEntity<ApiResponse<Map<String, Integer>>> countExamByExamCode(){
        return ResponseEntity.ok(examTypeService.getExamCountMap());
    }

    @GetMapping("/api/admin/exam-type/delete")
    public ResponseEntity<ApiResponse<List<ExamCodeResponse>>> getExamTypeIsDeleteTrue(){
        return ResponseEntity.ok(examTypeService.getExamTypesIsDeleteTrue());
    }

    @PutMapping("/api/admin/exam-type/{id}")
    public ResponseEntity<ApiResponse<ExamCodeResponse>> updateExamType(@PathVariable int id, @RequestBody UpdationExamCodeRequest request){
        return ResponseEntity.ok(examTypeService.updateExamType(id, request));
    }

    @PutMapping("/api/admin/exam-type/{id}/restore")
    public ResponseEntity<ApiResponse<ExamCodeResponse>> restoreExamType(@PathVariable int id){
        return ResponseEntity.ok(examTypeService.restoreExamType(id));
    }

    @DeleteMapping("/api/admin/exam-type/{id}")
    public ResponseEntity<ApiResponse<ExamCodeResponse>> deleteExamType(@PathVariable int id){
        return ResponseEntity.ok(examTypeService.deleteExamType(id));
    }

}

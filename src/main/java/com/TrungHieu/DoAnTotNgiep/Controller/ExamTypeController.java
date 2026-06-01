package com.TrungHieu.DoAnTotNgiep.Controller;

import com.TrungHieu.DoAnTotNgiep.DTO.Request.ExamTypeRequest.CreationExamTypeRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.ApiResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.ExamCodeResponse;
import com.TrungHieu.DoAnTotNgiep.Entity.ExamType;
import com.TrungHieu.DoAnTotNgiep.Service.ExamTypeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

}

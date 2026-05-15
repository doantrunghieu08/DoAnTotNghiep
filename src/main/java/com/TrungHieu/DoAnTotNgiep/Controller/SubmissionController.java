package com.TrungHieu.DoAnTotNgiep.Controller;

import com.TrungHieu.DoAnTotNgiep.DTO.Request.StudentSubmissionRequest.CreationSubissionRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.*;
import com.TrungHieu.DoAnTotNgiep.Service.StudentSubmissionService;
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
public class SubmissionController {
    StudentSubmissionService studentSubmissionService;

    @PostMapping("/student/submission")
    public ResponseEntity<ApiResponse<StudentSubmissionResponse>> creationSubmission(@RequestBody CreationSubissionRequest request){
        return ResponseEntity.ok(studentSubmissionService.createSubmission(request));
    }

    @GetMapping("/teacher/class/{classExamId}/submissions")
    public ResponseEntity<ApiResponse<List<SubmissionSummaryResponse>>> getClassSubmissions(@PathVariable int classExamId) {
        ApiResponse<List<SubmissionSummaryResponse>> response = studentSubmissionService.getClassSubmissionSummary(classExamId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/student/submission/{idStudent}")
    public ResponseEntity<ApiResponse<List<StudentSubmissionResponse>>> getSubmissions(@PathVariable String idStudent){
        return ResponseEntity.ok(studentSubmissionService.getExamStudent(idStudent));
    }

    @GetMapping("/public/grade-board/{classExamId}")
    public ResponseEntity<ApiResponse<List<SubmissionGradeDTO>>> getGradeBoard(
            @PathVariable Integer classExamId
    )
    {

        return ResponseEntity.ok(studentSubmissionService.getGradeBoard(classExamId));
    }

    @GetMapping("/public/grade-info/{submissionId}")
    public ResponseEntity<ApiResponse<SubmissionGradeDTO>> getGradeInfo(
            @PathVariable Integer submissionId
    )
    {
        return ResponseEntity.ok(studentSubmissionService.getGradeInfo(submissionId));
    }

    @GetMapping("/public/class-grade-board/submission/{classId}")
    public ResponseEntity<ApiResponse<List<StudentGradeBoardResponse>>> getClassGradeBoard(
            @PathVariable String classId
    ) {
        // Gọi service xử lý gom nhóm dữ liệu
        return ResponseEntity.ok(studentSubmissionService.getFullGradeBoard(classId));
    }

    @GetMapping("/student/my-exam/{idStudent}")
    public ResponseEntity<ApiResponse<Page<SubmissionGradeDTO>>> getMyExam(@PathVariable String idStudent, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(studentSubmissionService.getMyExams(idStudent, page, size));
    }
}

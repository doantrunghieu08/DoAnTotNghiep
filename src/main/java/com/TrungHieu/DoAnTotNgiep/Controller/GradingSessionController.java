package com.TrungHieu.DoAnTotNgiep.Controller;

import com.TrungHieu.DoAnTotNgiep.DTO.Request.ErrorRequest.AddErrorRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Request.ErrorRequest.CreationErrorRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Request.GradingSessionRequest.CreationGradingSessionRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.ApiResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.GradingErrorResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.GradingSessionResponse;
import com.TrungHieu.DoAnTotNgiep.Enum.GradingMode;
import com.TrungHieu.DoAnTotNgiep.Service.GradingErrorService;
import com.TrungHieu.DoAnTotNgiep.Service.GradingSessionService;
import com.cloudinary.Api;
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
public class GradingSessionController {
    GradingSessionService gradingSessionService;
    GradingErrorService gradingErrorService;

    @PostMapping("/teacher/grading-session/start")
    public ResponseEntity<ApiResponse<GradingSessionResponse>> creationGradingSession(@RequestBody CreationGradingSessionRequest request){
        return ResponseEntity.ok(gradingSessionService.createGradingSession(request));
    }

    @PostMapping("/teacher/grading-session/{idSession}/add-error")
    public ResponseEntity<ApiResponse<GradingErrorResponse>> addError(@PathVariable Integer idSession, @RequestBody AddErrorRequest request){
        return ResponseEntity.ok(gradingErrorService.addError(idSession, request));
    }

    @GetMapping("/public/grading-error/{idSession}")
    public ResponseEntity<ApiResponse<List<GradingErrorResponse>>> getError(@PathVariable int idSession, @RequestParam(defaultValue = "PRACTICE") GradingMode gradingMode){
        return ResponseEntity.ok(gradingErrorService.getError(idSession, gradingMode));
    }

    @PostMapping("/public/grading-session")
    public ResponseEntity<ApiResponse<GradingSessionResponse>> calculatorExam(@RequestBody CreationGradingSessionRequest request){
        return ResponseEntity.ok(gradingSessionService.calculatorTotalExam(request));
    }

    @PutMapping("/teacher/grading-session/{id}")
    public ResponseEntity<ApiResponse<GradingSessionResponse>> updateExam(@PathVariable int id){
        return ResponseEntity.ok(gradingSessionService.updateExam(id));
    }

    @GetMapping("/teacher/grading-session/submission/{idSubmission}")
    public ResponseEntity<ApiResponse<Page<GradingSessionResponse>>> getGradingSessionList(@PathVariable int idSubmission, @RequestParam int page, @RequestParam int size){
        return ResponseEntity.ok(gradingSessionService.getGradingSessionList(idSubmission, page, size));
    }

}

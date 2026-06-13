package com.TrungHieu.DoAnTotNgiep.Controller;

import com.TrungHieu.DoAnTotNgiep.DTO.Response.ApiResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.DashboardSummaryResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.SubmissionSummaryResponse;
import com.TrungHieu.DoAnTotNgiep.Service.ReportService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportController {
    ReportService reportService;

    @GetMapping("/public/api/reports/summary")
    public ResponseEntity<ApiResponse<DashboardSummaryResponse>> createSumaryReport(@RequestParam(value = "idTeacher") String idTeacher){
        return ResponseEntity.ok(reportService.reportSummary(idTeacher));
    }
}

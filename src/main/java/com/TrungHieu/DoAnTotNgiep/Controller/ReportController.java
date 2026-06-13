package com.TrungHieu.DoAnTotNgiep.Controller;

import com.TrungHieu.DoAnTotNgiep.DTO.Response.*;
import com.TrungHieu.DoAnTotNgiep.Service.ReportService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportController {
    ReportService reportService;

    @GetMapping("/public/api/reports/summary")
    public ResponseEntity<ApiResponse<DashboardSummaryResponse>> createSumaryReport(@RequestParam(value = "idTeacher") String idTeacher){
        return ResponseEntity.ok(reportService.reportSummary(idTeacher));
    }

    @GetMapping("/public/api/reports/errors-by-class")
    public ResponseEntity<ApiResponse<List<ErrorDistributionResponse>>> createErrorDistribution(@RequestParam(value = "idTeacher") String idTeacher){
        return ResponseEntity.ok(reportService.getErrorDistributionByClass(idTeacher));
    }

    @GetMapping("/public/api/reports/top-errors")
    public ResponseEntity<ApiResponse<List<TopErrorResponse>>> getTop5CommonErrors(@RequestParam(value = "idTeacher")String idTeacher){
        return ResponseEntity.ok(reportService.getTop5CommonErrors(idTeacher));
    }

}

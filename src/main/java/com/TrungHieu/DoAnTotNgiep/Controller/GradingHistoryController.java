package com.TrungHieu.DoAnTotNgiep.Controller;

import com.TrungHieu.DoAnTotNgiep.DTO.Response.ApiResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.GradingHistoryResponse;
import com.TrungHieu.DoAnTotNgiep.Service.GradingHistoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teacher/grading-session")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GradingHistoryController {

    GradingHistoryService gradingHistoryService;

    @GetMapping("/history/{teacherId}")
    public ApiResponse<Page<GradingHistoryResponse>> getHistory(
            @PathVariable String teacherId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return gradingHistoryService.getHistoryByTeacher(teacherId, page, size);
    }
}
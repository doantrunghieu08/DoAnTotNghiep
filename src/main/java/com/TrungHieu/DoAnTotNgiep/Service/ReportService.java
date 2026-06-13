package com.TrungHieu.DoAnTotNgiep.Service;

import com.TrungHieu.DoAnTotNgiep.DTO.Response.ApiResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.DashboardSummaryResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.ErrorDistributionResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.TopErrorResponse;
import com.TrungHieu.DoAnTotNgiep.Repository.ClassRepository;
import com.TrungHieu.DoAnTotNgiep.Repository.ErrorTypeRepository;
import com.TrungHieu.DoAnTotNgiep.Repository.GradingErrorRepository;
import com.TrungHieu.DoAnTotNgiep.Repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportService {
    ClassRepository classRepository;
    UserRepository userRepository;
    ErrorTypeRepository errorTypeRepository;
    GradingErrorRepository gradingErrorRepository;


    public ApiResponse<DashboardSummaryResponse> reportSummary(String idTeacher){
        Long totalClasses = classRepository.calculateTheTotalNumberOfClasses(idTeacher);
        Long totalStudent = userRepository.calculateTheTotalNumberOfStudentByTeacher(idTeacher);
        Long totalErrorTypes = errorTypeRepository.calculateErrorType(idTeacher);
        Long totalErrorOccurrences = gradingErrorRepository.calculateErrorInExam(idTeacher);
        DashboardSummaryResponse response = DashboardSummaryResponse.builder()
                .totalClasses(totalClasses)
                .totalStudents(totalStudent)
                .totalErrorTypes(totalErrorTypes)
                .totalErrorOccurrences(totalErrorOccurrences)
                .build();
        return new ApiResponse<>(200, "Tạo báo cáo thành công", response);
    }



    public ApiResponse<List<ErrorDistributionResponse>> getErrorDistributionByClass(String teacherId) {

        List<Object[]> results =
                errorTypeRepository.getErrorDistributionByClass(teacherId);

        return new ApiResponse<>(200, "Danh sách lỗi theo từng lớp", results.stream()
                .map(row -> new ErrorDistributionResponse(
                        (String) row[0],
                        (String) row[1],
                        ((Number) row[2]).longValue()
                ))
                .toList());
    }

    public ApiResponse<List<TopErrorResponse>> getTop5CommonErrors(String teacherId) {

        return new ApiResponse<>(200, "Top 5 loi pho bien", errorTypeRepository.getTop5CommonErrors(teacherId)
                .stream()
                .map(row -> new TopErrorResponse(
                        (String) row[0],
                        ((Number) row[1]).longValue()
                ))
                .toList());
    }
}

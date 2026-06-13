package com.TrungHieu.DoAnTotNgiep.Service;

import com.TrungHieu.DoAnTotNgiep.DTO.Response.ApiResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.DashboardSummaryResponse;
import com.TrungHieu.DoAnTotNgiep.Repository.ClassRepository;
import com.TrungHieu.DoAnTotNgiep.Repository.ErrorTypeRepository;
import com.TrungHieu.DoAnTotNgiep.Repository.GradingErrorRepository;
import com.TrungHieu.DoAnTotNgiep.Repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

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
}

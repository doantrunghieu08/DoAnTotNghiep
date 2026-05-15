package com.TrungHieu.DoAnTotNgiep.DTO.Response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StudentGradeBoardResponse {
    String studentId;
    String studentName;
    String studentCode;
    List<SubmissionGradeDTO> exams; // Danh sách 4 bài thi của sinh viên này
}

package com.TrungHieu.DoAnTotNgiep.DTO.Response;

import com.TrungHieu.DoAnTotNgiep.Enum.GradingSessionStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubmissionGradeDTO {
    // --- Thông tin định danh ---
    Integer submissionId;
    String studentName;
    String studentCode; // Username của sinh viên
    String avatarImage;

    // --- Đầu điểm LUYỆN TẬP (PRACTICE) ---
    Integer practiceSessionId;   // ID của phiên tập mới nhất để bấm vào xem lại
    Float practiceScore;         // Điểm của phiên tập mới nhất
    GradingSessionStatus practiceStatus; // Trạng thái (IN_PROGRESS/COMPLETED)

    // --- Đầu điểm CHẤM THẬT (OFFICIAL) ---
    Integer officialSessionId;   // ID của phiên thi mới nhất
    Float officialScore;         // Điểm của phiên thi mới nhất
    GradingSessionStatus officialStatus; // Trạng thái (IN_PROGRESS/COMPLETED)

    // --- Thông tin chung ---
    LocalDateTime lastUpdated;   // Thời gian cập nhật gần nhất (từ 1 trong 2 phiên)
}

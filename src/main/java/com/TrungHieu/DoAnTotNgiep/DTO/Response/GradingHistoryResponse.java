package com.TrungHieu.DoAnTotNgiep.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GradingHistoryResponse {
     String id;
     String teacherId;
     String teacherName;
     String classId;
     String className;
     String studentCode;
     String studentName;
     String examId;
     String examName;
     String gradingMode;     // "official" | "practice"
     String gradingSessionId;
     String submissionId;
     Instant gradedAt;       // timestampIso
     Double finalScore;
     Double totalDeductions;
     Integer totalFrames;

    List<GradingFrameResponse> frames;
}

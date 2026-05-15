package com.TrungHieu.DoAnTotNgiep.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class SubmissionSummaryResponse {
    String studentId;
    String fullName;
    String status;
    LocalDateTime submittedAt;
    Integer submissionId;
}

package com.TrungHieu.DoAnTotNgiep.DTO.Response;

import com.TrungHieu.DoAnTotNgiep.Enum.GradingMode;
import com.TrungHieu.DoAnTotNgiep.Enum.GradingSessionStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GradingSessionResponse {
    int id;

    StudentSubmissionResponse studentSubmissionResponse;

    UserResponse userResponse;

    GradingMode gradingMode;

    float totalDeduction;

    float finalScore;

    GradingSessionStatus status;

}

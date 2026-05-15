package com.TrungHieu.DoAnTotNgiep.DTO.Request.GradingSessionRequest;

import com.TrungHieu.DoAnTotNgiep.Enum.GradingMode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreationGradingSessionRequest {
    int idSubmission;
    String idTeacher;
    GradingMode gradingMode;
}

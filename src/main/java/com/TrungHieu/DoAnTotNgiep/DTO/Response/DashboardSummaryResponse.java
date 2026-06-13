package com.TrungHieu.DoAnTotNgiep.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class DashboardSummaryResponse {
    Long totalClasses;
    Long totalStudents;
    Long totalErrorTypes;
    Long totalErrorOccurrences;
}

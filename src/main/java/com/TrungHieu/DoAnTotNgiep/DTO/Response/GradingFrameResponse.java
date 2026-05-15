package com.TrungHieu.DoAnTotNgiep.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GradingFrameResponse {
    Integer errorId;
    Integer errorTypeId;
    String errorTypeName;
    String severity;
    float deductionApplied;
    String notes;
}

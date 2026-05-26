package com.TrungHieu.DoAnTotNgiep.DTO.Request.ErrorRequest;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ErrorCriteriaDto {
    int idError;
    String errorName;
    float deduction;
}

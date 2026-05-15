package com.TrungHieu.DoAnTotNgiep.DTO.Response;

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
public class GradingErrorResponse {
    // Để sếp render cột STT
    Integer id;

    String frameImageUrl;


    Double frameTimeSeconds;

    String errorName;
    String errorDescription;

    Float deduction;

}

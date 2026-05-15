package com.TrungHieu.DoAnTotNgiep.DTO.Request.ErrorRequest;

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
public class AddErrorRequest {
    Integer errorTypeId;

    float deduction;

    Double frameTime;

    String frameImageUrl;

    String notes;
}

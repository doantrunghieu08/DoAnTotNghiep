package com.TrungHieu.DoAnTotNgiep.DTO.Request.AIRequest;

import com.TrungHieu.DoAnTotNgiep.DTO.Request.ErrorRequest.ErrorCriteriaDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AiGradingRequest {
    private String videoUrl;
    private List<ErrorCriteriaDto> allowedErrors;

}

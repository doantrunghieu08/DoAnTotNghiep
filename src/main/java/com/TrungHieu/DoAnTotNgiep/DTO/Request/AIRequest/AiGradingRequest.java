package com.TrungHieu.DoAnTotNgiep.DTO.Request.AIRequest;

import com.TrungHieu.DoAnTotNgiep.DTO.Request.ErrorRequest.ErrorCriteriaDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class AiGradingRequest {
    String sampleVideoUrl;
    String videoUrl;
    List<ErrorCriteriaDto> allowedErrors;


}

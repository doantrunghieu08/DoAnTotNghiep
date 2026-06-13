package com.TrungHieu.DoAnTotNgiep.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDistributionResponse {
    private String classId;
    private String className;
    private Long totalErrors;
}

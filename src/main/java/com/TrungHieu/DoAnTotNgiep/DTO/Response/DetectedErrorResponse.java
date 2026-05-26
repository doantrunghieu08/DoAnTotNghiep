package com.TrungHieu.DoAnTotNgiep.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetectedErrorResponse {

    private ErrorResponse error;

    private Double timestamp;

    private String comment;

    private String frameImageUrl;
}
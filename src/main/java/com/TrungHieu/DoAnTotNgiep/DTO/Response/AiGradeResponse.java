package com.TrungHieu.DoAnTotNgiep.DTO.Response;

import java.util.List;

public class AiGradeResponse {
    private Double similarity;

    private Double suggestedScore;

    private List<DetectedErrorResponse> detectedErrors;
}

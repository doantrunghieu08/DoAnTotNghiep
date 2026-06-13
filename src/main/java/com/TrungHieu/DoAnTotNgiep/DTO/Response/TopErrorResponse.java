package com.TrungHieu.DoAnTotNgiep.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopErrorResponse {
    private String errorName;

    private Long totalOccurrences;
}

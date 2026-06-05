package com.TrungHieu.DoAnTotNgiep.DTO.Request.ExamRequest;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreationExamRequest {
    String name;

    String description;

    String sampleVideoUrl;

    String teacherId;

    int idExamCode;

    String standardData;
}

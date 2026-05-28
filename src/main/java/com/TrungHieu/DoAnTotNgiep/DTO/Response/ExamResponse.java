package com.TrungHieu.DoAnTotNgiep.DTO.Response;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExamResponse {
    int id;

    String idClass;

    LocalDateTime submissionDeadline;

    LocalDateTime gradingDeadline;

    ExamTypeResponse examTypeResponse;

    @JsonDeserialize(using = RawJsonDeserializer.class)
    String standardData;

}

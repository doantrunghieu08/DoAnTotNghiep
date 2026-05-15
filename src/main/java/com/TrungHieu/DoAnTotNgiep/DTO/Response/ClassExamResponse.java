package com.TrungHieu.DoAnTotNgiep.DTO.Response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassExamResponse {
    int id;

    String idClass;

    Integer idExam;

    LocalDateTime submissionDeadline;

    LocalDateTime gradingDeadline;
}

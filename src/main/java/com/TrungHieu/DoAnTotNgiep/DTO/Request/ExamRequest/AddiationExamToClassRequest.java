package com.TrungHieu.DoAnTotNgiep.DTO.Request.ExamRequest;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.aspectj.lang.annotation.Before;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddiationExamToClassRequest {
    Integer idExam;

    String idClass;

    @FutureOrPresent(message = "Ngày nộp phải là hiện tại hoặc trong tương lai")
    LocalDateTime submissionDeadline;

    @FutureOrPresent(message = "Ngày chấm phải là hiện tại hoặc trong tương lai")
    LocalDateTime gradingDeadline;
}

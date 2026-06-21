package com.TrungHieu.DoAnTotNgiep.DTO.Response;

import com.TrungHieu.DoAnTotNgiep.Enum.SubmittedStatus;
import jakarta.persistence.Column;
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
public class StudentSubmissionResponse {
    int id;

    int idClassExam;

    String refUrl;

    String standardData;

    String idStudent;

    String videoUrl1;

    long fileSizeBytes1;

    String videoUrl2;

    long fileSizeBytes2;

    LocalDateTime submittedAt;

    SubmittedStatus status;

    String studentData;
}

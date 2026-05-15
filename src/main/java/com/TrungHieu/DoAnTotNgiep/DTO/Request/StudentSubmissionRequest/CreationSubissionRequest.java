package com.TrungHieu.DoAnTotNgiep.DTO.Request.StudentSubmissionRequest;

import com.TrungHieu.DoAnTotNgiep.Entity.ClassExam;
import com.TrungHieu.DoAnTotNgiep.Entity.User;
import com.TrungHieu.DoAnTotNgiep.Enum.SubmittedStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreationSubissionRequest {

    int idClassExam;

    String idStudent;

    String videoUrl1;

    long fileSizeBytes1;

    String videoUrl2;

    long fileSizeBytes2;

    SubmittedStatus status;
}

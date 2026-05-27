package com.TrungHieu.DoAnTotNgiep.DTO.Request.StudentSubmissionRequest;

import com.TrungHieu.DoAnTotNgiep.Enum.SubmittedStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreationSubmissionRequest {

    int idClassExam;

    String idStudent;

    String videoUrl1;

    long fileSizeBytes1;

    String videoUrl2;

    long fileSizeBytes2;

    SubmittedStatus status;

    String studentData;
}

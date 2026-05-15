package com.TrungHieu.DoAnTotNgiep.DTO.Request.ErrorRequest;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CaptureFrameRequest {

    MultipartFile file;


    String studentName;


    int submissionId;
}

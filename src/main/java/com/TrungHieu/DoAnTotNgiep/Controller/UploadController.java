package com.TrungHieu.DoAnTotNgiep.Controller;

import com.TrungHieu.DoAnTotNgiep.DTO.Request.ErrorRequest.CaptureFrameRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Request.UserRequest.UploadAvatarRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.ApiResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.CaptureFrameResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.CloudinaryResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.UploadAvatarResponse;
import com.TrungHieu.DoAnTotNgiep.Service.CloudinaryServie;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UploadController {
    CloudinaryServie cloudinaryServie;

    @PostMapping("/public/upload-sample")
    public ResponseEntity<ApiResponse<String>> uploadSample(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title) {

        try {
            // 1. Gọi service lấy URL
            String videoUrl = cloudinaryServie.uploadSampleVideo(file, title);

            // 2. Trả về đúng chuẩn ApiResponse của sếp
            return ResponseEntity.ok(new ApiResponse<>(
                    200,
                    "Upload sample video successfully!",
                    videoUrl
            ));

        } catch (IOException e) {
            // Trả về lỗi hệ thống nếu upload thất bại
            return ResponseEntity.status(500).body(new ApiResponse<>(
                    500,
                    "Internal Server Error: " + e.getMessage(),
                    null
            ));
        }
    }
    @PostMapping("/public/upload-student-exam")
    public ResponseEntity<ApiResponse<CloudinaryResponse>> upload(@RequestParam("file") MultipartFile file,
                                                                  @RequestParam("studentName") String studentName,
                                                                  @RequestParam("examTitle") String examTitle) throws IOException {

        CloudinaryResponse response = cloudinaryServie.uploadStudentExamVideo(file, studentName, examTitle);

        return ResponseEntity.ok(new ApiResponse<>(200, "Upload thành công", response));
    }

    @PostMapping("/public/capture-error-frame")
    public ApiResponse<CaptureFrameResponse> capture(@ModelAttribute CaptureFrameRequest request) throws IOException {
        // Gọi service upload với folder: MilitaryProject/Error_Frames
        CloudinaryResponse cloudRes = cloudinaryServie.uploadErrorFrame(
                request.getFile(),
                request.getStudentName(),
                "Evidence_" + request.getSubmissionId()
        );

        CaptureFrameResponse response = CaptureFrameResponse.builder()
                .imageUrl(cloudRes.getUrl())
                .publicId(cloudRes.getPublicId())
                .size(cloudRes.getBytes())
                .build();

        return new ApiResponse<>(201, "Đã chốt khung hình bằng chứng", response);
    }

    @PostMapping("/public/upload-avatar")
    public ApiResponse<UploadAvatarResponse> uploadAvatar(@ModelAttribute UploadAvatarRequest request) throws IOException {
        CloudinaryResponse cloudRes = cloudinaryServie.uploadAvatar(
                request.getFile(),
                request.getId()
        );

        UploadAvatarResponse response = UploadAvatarResponse.builder()
                .imageUrl(cloudRes.getUrl())
                .publicId(cloudRes.getPublicId())
                .size(cloudRes.getBytes())
                .build();
        return new ApiResponse<>(201, "Tải avatar thành công", response);
    }
}

package com.TrungHieu.DoAnTotNgiep.Service;

import com.TrungHieu.DoAnTotNgiep.Cloudinary.CloudinaryConstant;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.CloudinaryResponse;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CloudinaryServie {
    Cloudinary cloudinary;

    public String uploadSampleVideo(MultipartFile file, String videoTitle) throws IOException {
        String publicId = videoTitle.trim().replaceAll("\\s+", "_").toLowerCase();

        Map result = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "resource_type", "video",
                        "folder", CloudinaryConstant.SAMPLE_VIDEO,
                        "public_id", publicId,
                        "overwrite", true
                ));

        return (String) result.get("secure_url");
    }

    public CloudinaryResponse uploadStudentExamVideo(MultipartFile file, String studentName, String examTitle) throws IOException {
        // 1. Tạo public_id theo tên sinh viên + tên bài thi + timestamp
        String rawId = studentName + "_" + examTitle + "_" + System.currentTimeMillis();
        String publicId = rawId.trim().replaceAll("\\s+", "_").toLowerCase();

        // 2. Thực hiện upload
        Map result = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "resource_type", "video",
                        "folder", CloudinaryConstant.STUDENT_VIDEO,
                        "public_id", publicId,
                        "overwrite", true
                ));

        // 3. Lấy dữ liệu và đóng gói vào Object
        return CloudinaryResponse.builder()
                .url((String) result.get("secure_url"))
                .bytes(((Number) result.get("bytes")).longValue()) // Lấy size thực tế từ server
                .build();
    }

    public CloudinaryResponse uploadErrorFrame(MultipartFile file, String studentName, String errorType) throws IOException {
        // 1. Tạo public_id: tên_sinh_vien + loại_lỗi + timestamp để không bị trùng
        String rawId = studentName + "_" + errorType + "_" + System.currentTimeMillis();
        String publicId = rawId.trim().replaceAll("\\s+", "_").toLowerCase();

        // 2. Thực hiện upload ảnh lên folder ROOT/Error_Frames
        Map result = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "resource_type", "image",
                        "folder", CloudinaryConstant.ERROR_FRAME, // Sử dụng hằng số folder của sếp
                        "public_id", publicId,
                        "overwrite", true
                ));

        // 3. Lấy dữ liệu và đóng gói vào Object (Tuân thủ theo cấu trúc sếp yêu cầu)
        return CloudinaryResponse.builder()
                .url((String) result.get("secure_url"))
                .publicId((String) result.get("public_id")) // Lưu publicId để sau này còn xóa
                .bytes(((Number) result.get("bytes")).longValue()) // Lấy size ảnh thực tế
                .build();
    }

    public CloudinaryResponse uploadAvatar(MultipartFile file, String userId) throws IOException {
        // 1. Tạo public_id duy nhất cho mỗi user (để tự động ghi đè ảnh cũ)
        String rawId = "avatar_" + userId;
        String publicId = rawId.trim().replaceAll("\\s+", "_").toLowerCase();

        // 2. Thực hiện upload lên folder đã định nghĩa trong Constant
        Map result = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "resource_type", "image",
                        "folder", CloudinaryConstant.USER_AVATAR, // Dùng hằng số của sếp đây
                        "public_id", publicId,
                        "overwrite", true,
                        // Tự động tối ưu: cắt vuông 200x200, lấy chính diện khuôn mặt
                        "transformation", new com.cloudinary.Transformation<>()
                                .width(200).height(200).crop("fill").gravity("face").quality("auto")
                ));

        // 3. Trả về đúng cấu trúc CloudinaryResponse
        return CloudinaryResponse.builder()
                .url((String) result.get("secure_url"))
                .publicId((String) result.get("public_id"))
                .bytes(((Number) result.get("bytes")).longValue())
                .build();
    }
}

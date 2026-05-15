package com.TrungHieu.DoAnTotNgiep.Service;

import com.TrungHieu.DoAnTotNgiep.DTO.Request.ExamRequest.AddiationExamToClassRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.*;
import com.TrungHieu.DoAnTotNgiep.Entity.ClassExam;
import com.TrungHieu.DoAnTotNgiep.Entity.Classes;
import com.TrungHieu.DoAnTotNgiep.Entity.ExamType;
import com.TrungHieu.DoAnTotNgiep.Entity.StudentSubmission;
import com.TrungHieu.DoAnTotNgiep.Exception.AppException;
import com.TrungHieu.DoAnTotNgiep.Exception.ErrorCode;
import com.TrungHieu.DoAnTotNgiep.Mapper.ClassExamMapper;
import com.TrungHieu.DoAnTotNgiep.Mapper.ExamTypeMapper;
import com.TrungHieu.DoAnTotNgiep.Repository.ClassExamRepository;
import com.TrungHieu.DoAnTotNgiep.Repository.ClassRepository;
import com.TrungHieu.DoAnTotNgiep.Repository.ExamTypeRepository;
import com.cloudinary.Api;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClassExamService {
    ClassRepository classRepository;
    ExamTypeRepository examTypeRepository;
    ClassExamRepository classExamRepository;
    ClassExamMapper classExamMapper;
    ExamTypeMapper examTypeMapper;

    @Transactional
    public ApiResponse<ClassExamResponse> addExamToClass(AddiationExamToClassRequest request){
        Classes classes = classRepository.findById(request.getIdClass())
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));
        ExamType examType = examTypeRepository.findById(request.getIdExam())
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));

        // Tạo mới hoàn toàn, không quan tâm cái cũ
        ClassExam classExam = new ClassExam();
        classExam.setClasses(classes);
        classExam.setExamType(examType);
        classExam.setSubmissionDeadline(request.getSubmissionDeadline());
        classExam.setGradingDeadline(request.getGradingDeadline());
        classExam.setIsDeleted(false); // Đảm bảo trạng thái là false

        classExamRepository.save(classExam);

        ClassExamResponse classExamResponse = classExamMapper.toResponse(classExam);
        return new ApiResponse<>(200, "Thêm bài thi vào lớp học thành công", classExamResponse);
    }

    public ApiResponse<Page<ExamResponse>> getExamByClassId(String idClass, int page, int size) {
        // 1. Tạo đối tượng phân trang (trang bắt đầu từ 0)
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        // 2. Gọi Repository lấy dữ liệu đã phân trang và lọc is_deleted = false
        Page<ClassExam> classExamsPage = classExamRepository.findByClasses_IdAndIsDeletedFalse(idClass, pageable);

        // 3. Map từ Entity sang Response DTO
        Page<ExamResponse> examResponses = classExamsPage.map(classExam -> {
            ExamResponse response = classExamMapper.toExamResponse(classExam);
            // Sếp có thể bổ sung thêm logic check trạng thái bài thi ở đây nếu cần
            return response;
        });

        return new ApiResponse<>(200, "Lấy danh sách bài thi thành công", examResponses);
    }

    @Transactional
    public void deletedExam(String idClass, int idExam){
        classExamRepository.deleteExamByClassIdAndExamId(idClass, idExam);
    }

    public ApiResponse<Page<ExamResponse>> getDeletedExamsByClassId(String idClass, int page, int size) {
        // 1. Cấu hình phân trang (Trang hiện tại, số lượng mỗi trang, sắp xếp theo ID giảm dần)
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        // 2. Truy vấn Database lấy danh sách bài thi có isDeleted = true
        Page<ClassExam> deletedExamsPage = classExamRepository.findByClasses_IdAndIsDeletedTrue(idClass, pageable);

        // 3. Map sang DTO để trả về cho Frontend
        Page<ExamResponse> examResponses = deletedExamsPage.map(classExam -> {
            return classExamMapper.toExamResponse(classExam);
        });

        return new ApiResponse<>(200, "Lấy danh sách bài thi đã xóa thành công", examResponses);
    }


    @Transactional
    public ApiResponse<ClassExamResponse> restoreExam(String idClass, int idExam) {
        // 1. Tìm bản ghi (đảm bảo đã xóa @Where trong Entity)
        ClassExam classExam = classExamRepository.findByClasses_IdAndExamType_Id(idClass, idExam)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));

        // 2. Kiểm tra trạng thái
        if (Boolean.FALSE.equals(classExam.getIsDeleted())) {
            return new ApiResponse<>(400, "Bài thi này hiện đang hoạt động", null);
        }

        // 3. Khôi phục
        classExam.setIsDeleted(false);
        classExamRepository.save(classExam);

        return new ApiResponse<>(200, "Khôi phục thành công", classExamMapper.toResponse(classExam));
    }





}

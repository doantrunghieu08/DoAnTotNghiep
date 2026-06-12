package com.TrungHieu.DoAnTotNgiep.Service;

import com.TrungHieu.DoAnTotNgiep.DTO.Request.ExamRequest.CreationExamRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Request.ExamRequest.UpdationExamRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.ApiResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.ExamTypeResponse;
import com.TrungHieu.DoAnTotNgiep.Entity.Exam;
import com.TrungHieu.DoAnTotNgiep.Entity.ExamType;
import com.TrungHieu.DoAnTotNgiep.Entity.User;
import com.TrungHieu.DoAnTotNgiep.Enum.RoleType;
import com.TrungHieu.DoAnTotNgiep.Exception.AppException;
import com.TrungHieu.DoAnTotNgiep.Exception.ErrorCode;
import com.TrungHieu.DoAnTotNgiep.Mapper.ExamMapper;
import com.TrungHieu.DoAnTotNgiep.Repository.ClassRepository;
import com.TrungHieu.DoAnTotNgiep.Repository.ExamRepository;
import com.TrungHieu.DoAnTotNgiep.Repository.ExamTypeRepository;
import com.TrungHieu.DoAnTotNgiep.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExamService {
    UserRepository userRepository;
    ExamRepository examRepository;
    ExamMapper examMapper;
    ClassRepository classRepository;
    ExamTypeRepository examTypeRepository;

    public ApiResponse<Page<ExamTypeResponse>> getAllExamByTeacherId(String idTeacher, int page, int size) {

        User user = userRepository.findById(idTeacher)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));


        if (!RoleType.TEACHER.equals(user.getRole())) {
            return new ApiResponse<>(ErrorCode.INVALID_ROLE.getCode(), ErrorCode.INVALID_ROLE.getMessage(), null);
        }


        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());


        Page<Exam> examTypePage = examRepository.findAllByTeacher(user, pageable);


        Page<ExamTypeResponse> examTypeResponses = examTypePage.map(examMapper::toResponse);

        return new ApiResponse<>(200, "Danh sách bài thi của giáo viên: " + user.getFullName(), examTypeResponses);
    }

    public ApiResponse<ExamTypeResponse> creationExam(CreationExamRequest request){
        Exam exam = new Exam();
        User teacher = userRepository.findById(request.getTeacherId()).orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));
        if (!RoleType.TEACHER.equals(teacher.getRole())){
            return new ApiResponse<>(ErrorCode.INVALID_ROLE.getCode(), ErrorCode.INVALID_ROLE.getMessage(), null);
        }
        exam.setName(request.getName());
        exam.setDescription(request.getDescription());
        exam.setSampleVideoUrl(request.getSampleVideoUrl());
        exam.setTeacher(teacher);
        ExamType examType = examTypeRepository.findById(request.getIdExamCode()).orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));
        exam.setExamType(examType);
        exam.setStandardData(request.getStandardData());
        exam.setCreatedAt(LocalDateTime.now());
        examRepository.save(exam);
        return new ApiResponse<>(201, "Tao bai thi thanh cong", examMapper.toResponse(exam));
    }


    @Transactional
    public void deleteExam(int id){
        Exam exam = examRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));
        exam.setDeleted(true);
        examRepository.save(exam);
    }

    @Transactional
    public void restoreExam(int id){
        Exam exam = examRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));
        exam.setDeleted(false);
        examRepository.save(exam);
    }

    @Transactional
    public ApiResponse<ExamTypeResponse> updateExam(int idExam, UpdationExamRequest request){
        Exam exam = examRepository.findById(idExam)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));

        // 1. Cập nhật ExamType nếu phía Frontend có truyền idExamType mới
        if (request.getIdExamType() != 0) { // hoặc != null nếu dùng kiểu Integer
            ExamType examType = examTypeRepository.findById(request.getIdExamType())
                    .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));
            exam.setExamType(examType); // Nhớ gán lại mối quan hệ cho đúng
        }

        // 2. Kiểm tra và giữ nguyên giá trị nếu không cập nhật
        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            exam.setName(request.getName());
        }

        if (request.getDescription() != null) { // Cho phép description truyền chuỗi rỗng nếu muốn xóa mô tả
            exam.setDescription(request.getDescription());
        }

        if (request.getSampleVideoUrl() != null && !request.getSampleVideoUrl().trim().isEmpty()) {
            exam.setSampleVideoUrl(request.getSampleVideoUrl());
            exam.setStandardData(request.getStandardData());
        }

        Exam saved = examRepository.save(exam);
        return new ApiResponse<>(200, "Cập nhật thành công", examMapper.toResponse(saved));
    }




}

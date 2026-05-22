package com.TrungHieu.DoAnTotNgiep.Service;

import com.TrungHieu.DoAnTotNgiep.DTO.Request.ExamRequest.CreationExamRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.ApiResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.ExamTypeResponse;
import com.TrungHieu.DoAnTotNgiep.Entity.Exam;
import com.TrungHieu.DoAnTotNgiep.Entity.User;
import com.TrungHieu.DoAnTotNgiep.Enum.RoleType;
import com.TrungHieu.DoAnTotNgiep.Exception.AppException;
import com.TrungHieu.DoAnTotNgiep.Exception.ErrorCode;
import com.TrungHieu.DoAnTotNgiep.Mapper.ExamTypeMapper;
import com.TrungHieu.DoAnTotNgiep.Repository.ClassRepository;
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

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExamTypeService {
    UserRepository userRepository;
    ExamTypeRepository examTypeRepository;
    ExamTypeMapper examTypeMapper;
    ClassRepository classRepository;

    public ApiResponse<Page<ExamTypeResponse>> getAllExamByTeacherId(String idTeacher, int page, int size) {

        User user = userRepository.findById(idTeacher)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));


        if (!RoleType.TEACHER.equals(user.getRole())) {
            return new ApiResponse<>(ErrorCode.INVALID_ROLE.getCode(), ErrorCode.INVALID_ROLE.getMessage(), null);
        }


        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());


        Page<Exam> examTypePage = examTypeRepository.findAllByTeacher(user, pageable);


        Page<ExamTypeResponse> examTypeResponses = examTypePage.map(examTypeMapper::toResponse);

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
        examTypeRepository.save(exam);
        return new ApiResponse<>(201, "Tao bai thi thanh cong", examTypeMapper.toResponse(exam));
    }


    @Transactional
    public void deleteExam(int id){
        Exam exam = examTypeRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));
        exam.setDeleted(true);
        examTypeRepository.save(exam);
    }

    @Transactional
    public void restoreExam(int id){
        Exam exam = examTypeRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));
        exam.setDeleted(false);
        examTypeRepository.save(exam);
    }




}

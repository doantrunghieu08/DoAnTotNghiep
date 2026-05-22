package com.TrungHieu.DoAnTotNgiep.Service;

import com.TrungHieu.DoAnTotNgiep.DTO.Request.ExamTypeRequest.CreationExamTypeRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.ApiResponse;
import com.TrungHieu.DoAnTotNgiep.Entity.ExamType;
import com.TrungHieu.DoAnTotNgiep.Repository.ExamTypeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExamTypeService {
    ExamTypeRepository examTypeRepository;
    public ApiResponse<ExamType> creationExam(CreationExamTypeRequest request){
        ExamType examType = new ExamType();
        examType.setExamCode(request.getExamCode());
        examTypeRepository.save(examType);
        return new ApiResponse<>(200, "Thêm loại bài thi thành công", examType);
    }


}

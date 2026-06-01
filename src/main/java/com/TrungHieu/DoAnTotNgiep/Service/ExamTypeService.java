package com.TrungHieu.DoAnTotNgiep.Service;

import com.TrungHieu.DoAnTotNgiep.DTO.Request.ExamTypeRequest.CreationExamTypeRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.ApiResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.ExamCodeResponse;
import com.TrungHieu.DoAnTotNgiep.Entity.ExamType;
import com.TrungHieu.DoAnTotNgiep.Mapper.ExamTypeMapper;
import com.TrungHieu.DoAnTotNgiep.Repository.ExamTypeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExamTypeService {
    ExamTypeRepository examTypeRepository;
    ExamTypeMapper examTypeMapper;

    public ApiResponse<ExamCodeResponse> creationExamType(CreationExamTypeRequest request){
        ExamType examType = new ExamType();
        examType.setExamCode(request.getExamCode());
        examTypeRepository.save(examType);

        return new ApiResponse<>(200, "Thêm loại bài thi thành công", examTypeMapper.toResponse(examType));
    }

    public ApiResponse<List<ExamCodeResponse>> getExamTypes(){
        List<ExamType> examTypes = examTypeRepository.findAll();
        return new ApiResponse<>(200, "Danh sach loai bai thi", examTypeMapper.toResponseList(examTypes));
    }

    public ApiResponse<Map<String, Integer>> getExamCountMap(){
        List<Map<String, Object>> rows = examTypeRepository.countExamByExamCode();

        return new ApiResponse<>(200, "Lấy số lượng thành công",rows.stream().collect(Collectors.toMap(
                row -> (String) row.get("exam_code"),
                row -> ((Number) row.get("SLBaiThi")).intValue()
        )));
    }




}

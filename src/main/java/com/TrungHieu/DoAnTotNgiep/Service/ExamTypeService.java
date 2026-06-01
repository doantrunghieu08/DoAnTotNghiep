package com.TrungHieu.DoAnTotNgiep.Service;

import com.TrungHieu.DoAnTotNgiep.DTO.Request.ExamTypeRequest.CreationExamTypeRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Request.ExamTypeRequest.UpdationExamCodeRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.ApiResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.ExamCodeResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.ExamTypeResponse;
import com.TrungHieu.DoAnTotNgiep.Entity.ExamType;
import com.TrungHieu.DoAnTotNgiep.Exception.AppException;
import com.TrungHieu.DoAnTotNgiep.Exception.ErrorCode;
import com.TrungHieu.DoAnTotNgiep.Mapper.ExamTypeMapper;
import com.TrungHieu.DoAnTotNgiep.Repository.ExamTypeRepository;
import com.cloudinary.Api;
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
        List<ExamType> examTypes = examTypeRepository.findByIsDeletedFalse();
        return new ApiResponse<>(200, "Danh sach loai bai thi", examTypeMapper.toResponseList(examTypes));
    }

    public ApiResponse<List<ExamCodeResponse>> getExamTypesIsDeleteTrue(){
        List<ExamType> examTypes = examTypeRepository.findByIsDeletedTrue();
        return new ApiResponse<>(200, "Danh sach loai bai thi da xoa", examTypeMapper.toResponseList(examTypes));
    }

    public ApiResponse<ExamCodeResponse> restoreExamType(int id){
        ExamType examType = examTypeRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));
        if (examType.isDeleted() == false){
            return new ApiResponse<>(200, "Lỗi này chưa có trong danh sách xóa", examTypeMapper.toResponse(examType));
        }
        examType.setDeleted(false);
        return new ApiResponse<>(200, "Khoi phuc thanh cong", examTypeMapper.toResponse(examType));
    }

    public ApiResponse<ExamCodeResponse> deleteExamType(int id){
        ExamType examType = examTypeRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));
        if (examType.isDeleted() == true){
            return new ApiResponse<>(200, "Lỗi này đã có trong danh sách xóa", examTypeMapper.toResponse(examType));
        }
        examType.setDeleted(true);
        return new ApiResponse<>(200, "Xóa thành công", examTypeMapper.toResponse(examType));
    }

    public ApiResponse<ExamCodeResponse> updateExamType(int id, UpdationExamCodeRequest request){
        ExamType examType = examTypeRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));
        examType.setExamCode(request.getExamCode());
        return new ApiResponse<>(200, "Cap nhat thanh cong", examTypeMapper.toResponse(examType));
    }

    public ApiResponse<Map<String, Integer>> getExamCountMap(){
        List<Map<String, Object>> rows = examTypeRepository.countExamByExamCode();

        return new ApiResponse<>(200, "Lấy số lượng thành công",rows.stream().collect(Collectors.toMap(
                row -> (String) row.get("exam_code"),
                row -> ((Number) row.get("SLBaiThi")).intValue()
        )));
    }




}

package com.TrungHieu.DoAnTotNgiep.Service;

import com.TrungHieu.DoAnTotNgiep.DTO.Request.ErrorRequest.CreationErrorRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.ApiResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.ErrorResponse;
import com.TrungHieu.DoAnTotNgiep.Entity.ErrorType;
import com.TrungHieu.DoAnTotNgiep.Entity.User;
import com.TrungHieu.DoAnTotNgiep.Enum.RoleType;
import com.TrungHieu.DoAnTotNgiep.Exception.AppException;
import com.TrungHieu.DoAnTotNgiep.Exception.ErrorCode;
import com.TrungHieu.DoAnTotNgiep.Mapper.ErrorMapper;
import com.TrungHieu.DoAnTotNgiep.Repository.ErrorTypeRepository;
import com.TrungHieu.DoAnTotNgiep.Repository.UserRepository;
import com.cloudinary.Api;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ErrorTypeService {
    ErrorTypeRepository errorTypeRepository;
    UserRepository userRepository;
    ErrorMapper errorMapper;

    @CacheEvict(value = "errors", allEntries = true)
    public ApiResponse<ErrorResponse> creationError(CreationErrorRequest request){
        User teacher = userRepository.findById(request.getIdTeacher()).orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));
        ErrorType errorType = new ErrorType();
        errorType.setTeacher(teacher);
        errorType.setName(request.getName());
        errorType.setDescription(request.getDescription());
        errorType.setSeverity(request.getSeverityType());
        errorType.setDeduction(request.getDeduction());
        errorType.setActive(true);

        errorTypeRepository.save(errorType);
        return new ApiResponse<>(201, "Them loi thanh cong", errorMapper.toResponse(errorType));

    }

    @Cacheable(value = "errors", key = "#idTeacher + '_' + #page + '_' + #size")
    public ApiResponse<Page<ErrorResponse>> getErrors(String idTeacher, int page, int size) {
        // 1. Kiểm tra sự tồn tại của User
        User user = userRepository.findById(idTeacher)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));

        // 2. Chặn quyền ngay từ cửa
        if (!RoleType.TEACHER.equals(user.getRole())) {
            return new ApiResponse<>(ErrorCode.INVALID_ROLE.getCode(), ErrorCode.INVALID_ROLE.getMessage(), null);
        }

        // 3. Cấu hình Pageable (Sắp xếp theo ID giảm dần để lỗi mới tạo hiện lên trước)
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        // 4. Lấy dữ liệu phân trang
        Page<ErrorType> errorTypePage = errorTypeRepository.findAllByTeacher(user, pageable);

        // 5. Chuyển đổi sang Response DTO bằng Map (giữ nguyên cấu trúc Page)
        Page<ErrorResponse> errorResponses = errorTypePage.map(errorMapper::toResponse);


        return new ApiResponse<>(200, "Lấy danh sách lỗi thành công", errorResponses);
    }

    @Transactional
    @CacheEvict(value = "errors", allEntries = true)
    public void deleteError(int id){

        ErrorType errorType = errorTypeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));

        errorType.setActive(false);
        errorTypeRepository.save(errorType);
    }

    @Transactional
    @CacheEvict(value = "errors", allEntries = true)
    public void restoreError(int id){
        ErrorType errorType = errorTypeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));

        errorType.setActive(true);
        errorTypeRepository.save(errorType);
    }
}

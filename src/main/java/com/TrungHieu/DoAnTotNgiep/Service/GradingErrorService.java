package com.TrungHieu.DoAnTotNgiep.Service;

import com.TrungHieu.DoAnTotNgiep.DTO.Request.ErrorRequest.AddErrorRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.ApiResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.GradingErrorResponse;
import com.TrungHieu.DoAnTotNgiep.Entity.ErrorType;
import com.TrungHieu.DoAnTotNgiep.Entity.GradingError;
import com.TrungHieu.DoAnTotNgiep.Entity.GradingSession;
import com.TrungHieu.DoAnTotNgiep.Enum.GradingMode;
import com.TrungHieu.DoAnTotNgiep.Enum.GradingSessionStatus;
import com.TrungHieu.DoAnTotNgiep.Exception.AppException;
import com.TrungHieu.DoAnTotNgiep.Exception.ErrorCode;
import com.TrungHieu.DoAnTotNgiep.Mapper.GradingErrorMapper;
import com.TrungHieu.DoAnTotNgiep.Mapper.GradingSessionMapper;
import com.TrungHieu.DoAnTotNgiep.Repository.ErrorTypeRepository;
import com.TrungHieu.DoAnTotNgiep.Repository.GradingErrorRepository;
import com.TrungHieu.DoAnTotNgiep.Repository.GradingSessionRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GradingErrorService {
    GradingSessionRepository gradingSessionRepository;
    GradingErrorRepository gradingErrorRepository;
    GradingErrorMapper gradingErrorMapper;
    ErrorTypeRepository errorTypeRepository;

    @Transactional
    public ApiResponse<GradingErrorResponse> addError(int idSession, AddErrorRequest request) {
        // 1. Tìm Session
        GradingSession gradingSession = gradingSessionRepository.findById(idSession)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));

        // 2. Chặn nếu đã chốt điểm
        if (GradingSessionStatus.COMPLETED.equals(gradingSession.getStatus())) {
            throw new AppException(ErrorCode.SESSION_ALREADY_FINALIZED);
        }

        // 3. Tìm loại lỗi
        ErrorType errorType = errorTypeRepository.findById(request.getErrorTypeId())
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));

        // 4. Map dữ liệu vào Entity
        GradingError gradingError = new GradingError();
        gradingError.setErrorType(errorType);
        gradingError.setSession(gradingSession);
        gradingError.setFrameTimeSeconds(request.getFrameTime());
        gradingError.setFrameImageUrl(request.getFrameImageUrl());
        gradingError.setNotes(request.getNotes());

        // Đảm bảo deduction không bị null
        float deduction = request.getDeduction() != 0 ? request.getDeduction() : 0;
        gradingError.setDeductionApplied(deduction);

        // 5. Lưu lỗi (Sếp nhớ dùng kết quả trả về của hàm save nhé)
        GradingError savedError = gradingErrorRepository.save(gradingError);

        // 6. Cập nhật tổng điểm trừ vào Session
        float totalDeduction = gradingErrorRepository.sumDeductionBySessionId(idSession);
        gradingSession.setTotalDeduction(totalDeduction != 0 ? totalDeduction : 0);
        gradingSessionRepository.save(gradingSession);

        gradingSessionRepository.save(gradingSession);

        // 7. Trả về Response (Dùng savedError để có ID cho Frontend)
        return new ApiResponse<>(201, "Thêm lỗi thành công", gradingErrorMapper.toResponse(savedError));
    }

    public ApiResponse<List<GradingErrorResponse>> getError(int idSession, GradingMode gradingMode){
        GradingSession gradingSession = gradingSessionRepository.findByIdAndGradingMode(idSession, gradingMode).orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));
//        if (gradingSession.getStatus().equals(GradingSessionStatus.COMPLETED)){
//            throw new AppException(ErrorCode.)
//        }
        List<GradingError> gradingErrors = gradingSession.getGradingErrors();
        return new ApiResponse<>(200, "Chi tiet loi", gradingErrorMapper.toResponseList(gradingErrors));
    }
}

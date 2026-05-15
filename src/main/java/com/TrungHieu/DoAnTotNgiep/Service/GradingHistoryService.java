package com.TrungHieu.DoAnTotNgiep.Service;

import com.TrungHieu.DoAnTotNgiep.DTO.Response.ApiResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.GradingFrameResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.GradingHistoryResponse;
import com.TrungHieu.DoAnTotNgiep.Entity.GradingError;
import com.TrungHieu.DoAnTotNgiep.Entity.GradingSession;
import com.TrungHieu.DoAnTotNgiep.Exception.AppException;
import com.TrungHieu.DoAnTotNgiep.Exception.ErrorCode;
import com.TrungHieu.DoAnTotNgiep.Mapper.GradingHistoryMapper;
import com.TrungHieu.DoAnTotNgiep.Repository.GradingErrorRepository;
import com.TrungHieu.DoAnTotNgiep.Repository.GradingSessionRepository;
import com.TrungHieu.DoAnTotNgiep.Repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GradingHistoryService {

    GradingSessionRepository gradingSessionRepository;
    GradingErrorRepository gradingErrorRepository;
    UserRepository userRepository;
    GradingHistoryMapper gradingHistoryMapper;


    public ApiResponse<Page<GradingHistoryResponse>> getHistoryByTeacher(String teacherId, int page, int size) {
        // 1. Kiểm tra User
        userRepository.findById(teacherId)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));

        // 2. Thiết lập phân trang (Lịch sử mới nhất thường để ID giảm dần)
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        // 3. Lấy dữ liệu phân trang cho Sessions
        Page<GradingSession> sessionsPage = gradingSessionRepository.findDetailsByTeacherId(teacherId, pageable);

        if (sessionsPage.isEmpty()) {
            return new ApiResponse<>(200, "Chưa có lịch sử chấm", Page.empty());
        }

        // 4. Lấy danh sách ID của TRANG HIỆN TẠI (Chỉ 10-20 IDs tùy size)
        List<Integer> sessionIds = sessionsPage.getContent().stream()
                .map(GradingSession::getId)
                .collect(Collectors.toList());

        // 5. Gom nhóm lỗi (Giữ nguyên logic tối ưu của sếp)
        Map<Integer, List<GradingError>> errorsBySession =
                gradingErrorRepository.findWithErrorTypeBySessionIdIn(sessionIds)
                        .stream()
                        .collect(Collectors.groupingBy(e -> e.getSession().getId()));

        // 6. Map sang Response DTO (Dùng .map của Page để giữ metadata phân trang)
        Page<GradingHistoryResponse> result = sessionsPage.map(session -> {
            GradingHistoryResponse response = gradingHistoryMapper.toResponse(session);
            List<GradingError> errors = errorsBySession.getOrDefault(session.getId(), List.of());
            response.setFrames(buildFrames(errors));
            return response;
        });

        return new ApiResponse<>(200, "Lấy lịch sử chấm thành công", result);
    }

    private List<GradingFrameResponse> buildFrames(List<GradingError> errors) {
        return errors.stream()
                .map(gradingHistoryMapper::toFrameResponse)
                .collect(Collectors.toList());
    }
}
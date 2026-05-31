package com.TrungHieu.DoAnTotNgiep.Service;

import com.TrungHieu.DoAnTotNgiep.DTO.Request.ErrorRequest.CreationErrorRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Request.GradingSessionRequest.CreationGradingSessionRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.ApiResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.GradingSessionResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.StudentSubmissionResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.UserResponse;
import com.TrungHieu.DoAnTotNgiep.Entity.*;
import com.TrungHieu.DoAnTotNgiep.Enum.GradingMode;
import com.TrungHieu.DoAnTotNgiep.Enum.GradingSessionStatus;
import com.TrungHieu.DoAnTotNgiep.Exception.AppException;
import com.TrungHieu.DoAnTotNgiep.Exception.ErrorCode;
import com.TrungHieu.DoAnTotNgiep.Mapper.GradingSessionMapper;
import com.TrungHieu.DoAnTotNgiep.Repository.GradingErrorRepository;
import com.TrungHieu.DoAnTotNgiep.Repository.GradingSessionRepository;
import com.TrungHieu.DoAnTotNgiep.Repository.StudentSubmissionRepository;
import com.TrungHieu.DoAnTotNgiep.Repository.UserRepository;
import com.cloudinary.Api;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GradingSessionService {
    GradingSessionRepository gradingSessionRepository;
    StudentSubmissionRepository studentSubmissionRepository;
    UserRepository userRepository;
    GradingSessionMapper gradingSessionMapper;


    @Transactional
    public ApiResponse<GradingSessionResponse> createGradingSession(CreationGradingSessionRequest request) {
        // 1. Kiểm tra bài nộp và giáo viên tồn tại hay không
        StudentSubmission submission = studentSubmissionRepository.findById(request.getIdSubmission())
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));
        User teacher = userRepository.findById(request.getIdTeacher())
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));


        // 2. Kiểm tra xem đã có phiên nào cùng MODE và đang "IN_PROGRESS" chưa
        Optional<GradingSession> existingSession = gradingSessionRepository
                .findFirstBySubmissionIdAndTeacherIdAndStatusAndGradingMode( // Thêm Mode vào đây
                        request.getIdSubmission(),
                        request.getIdTeacher(),
                        GradingSessionStatus.IN_PROGRESS,
                        request.getGradingMode() // Truyền mode từ request vào
                );

        // 3. Nếu tìm thấy, sếp trả về ngay phiên đó luôn (Vào lại phiên cũ)
        if (existingSession.isPresent()) {
            GradingSessionResponse response = gradingSessionMapper.toResponse(existingSession.get());
            
            StudentSubmissionResponse studentSubmissionResponse = response.getStudentSubmissionResponse();
            studentSubmissionResponse.setStandardData(submission.getClassExam().getExam().getStandardData());
            response.setStudentSubmissionResponse(studentSubmissionResponse);

            return new ApiResponse<>(200, "Vào lại phiên chấm hiện tại", response);
        }



        // 4. Nếu không có phiên nào đang dang dở, sếp mới tạo mới
        GradingSession gradingSession = new GradingSession();
        gradingSession.setSubmission(submission);
        gradingSession.setTeacher(teacher);
        gradingSession.setGradingMode(request.getGradingMode());
        gradingSession.setStatus(GradingSessionStatus.IN_PROGRESS);
        gradingSession.setGradedAt(Instant.now());

        GradingSession savedSession = gradingSessionRepository.saveAndFlush(gradingSession);
        GradingSessionResponse response = gradingSessionMapper.toResponse(savedSession);

        StudentSubmissionResponse studentSubmissionResponse = response.getStudentSubmissionResponse();
        studentSubmissionResponse.setStandardData(submission.getClassExam().getExam().getStandardData());


        response.setStudentSubmissionResponse(studentSubmissionResponse);

        return new ApiResponse<>(201, "Khởi tạo phiên chấm mới",
                response);
    }

    @Transactional
    public ApiResponse<GradingSessionResponse> calculatorTotalExam(CreationGradingSessionRequest request) {
        // 1. Tìm đúng phiên đang chấm dựa trên Submission, Teacher, Status và Mode
        GradingSession session = gradingSessionRepository
                .findFirstBySubmissionIdAndTeacherIdAndStatusAndGradingMode(
                        request.getIdSubmission(),
                        request.getIdTeacher(),
                        GradingSessionStatus.IN_PROGRESS,
                        request.getGradingMode()
                ).orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));

        // 2. Tính tổng điểm trừ từ danh sách lỗi
        List<GradingError> errors = session.getGradingErrors();
        double sumDeduction = errors.stream()
                .mapToDouble(GradingError::getDeductionApplied)
                .sum();

        // 3. Tính điểm tổng kết (Đảm bảo không bị điểm âm)
        float totalExam = (float) Math.max(0.0, 10.0 - sumDeduction);

        // 4. Cập nhật vào Session (Lưu ý: đặt tên method theo camelCase cho chuẩn Java nhé sếp: setFinalScore)
        session.setFinalScore(totalExam);

        // 5. Nếu sếp muốn chốt luôn khi tính điểm thì set status sang FINALIZED ở đây
        // session.setStatus(GradingSessionStatus.FINALIZED);
        session.setStatus(GradingSessionStatus.COMPLETED);

        GradingSession savedSession = gradingSessionRepository.save(session);

        return new ApiResponse<>(200, "Tính điểm thành công", gradingSessionMapper.toResponse(savedSession));
    }


    public ApiResponse<GradingSessionResponse> updateExam(int id) {
        GradingSession gradingSession = gradingSessionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));

        // 1. CHỐT CHẶN VĨNH VIỄN: Nếu đã FINALIZED thì không làm gì thêm
        if (GradingSessionStatus.FINALIZED.equals(gradingSession.getStatus())) {
            throw new AppException(ErrorCode.SESSION_ALREADY_FINALIZED);
            // Sếp định nghĩa ErrorCode này là: "Phiên chấm đã chốt, không thể thay đổi dữ liệu."
        }

        ClassExam classExam = gradingSession.getSubmission().getClassExam();

        // 2. Kiểm tra hạn chấm (như sếp muốn)
        if (LocalDateTime.now().isBefore(classExam.getGradingDeadline())) {
            gradingSession.setStatus(GradingSessionStatus.FINALIZED);
            gradingSessionRepository.save(gradingSession);
        } else {
            // Nếu quá hạn thì có thể báo lỗi hoặc xử lý kiểu khác
            throw new AppException(ErrorCode.GRADING_PERIOD_ENDED);
        }

        return new ApiResponse<>(200, "Đã chốt điểm thành công. Không thể chỉnh sửa thêm.",
                gradingSessionMapper.toResponse(gradingSession));
    }

    public ApiResponse<Page<GradingSessionResponse>> getGradingSessionList(int submissionId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<GradingSession> gradingSessionPage = gradingSessionRepository
                .findAllBySubmissionIdAndStatusIn(
                        submissionId,
                        List.of(GradingSessionStatus.IN_PROGRESS, GradingSessionStatus.COMPLETED),
                        pageable
                );

        Page<GradingSessionResponse> gradingSessionResponses = gradingSessionPage.map(gradingSessionMapper::toResponse);

        return new ApiResponse<>(200, "Lấy danh sách phiên chấm thành công", gradingSessionResponses);
    }
}

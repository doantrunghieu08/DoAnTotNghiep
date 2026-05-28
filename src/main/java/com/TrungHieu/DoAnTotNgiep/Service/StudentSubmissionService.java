package com.TrungHieu.DoAnTotNgiep.Service;

import com.TrungHieu.DoAnTotNgiep.DTO.Request.StudentSubmissionRequest.CreationSubmissionRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.*;
import com.TrungHieu.DoAnTotNgiep.Entity.*;
import com.TrungHieu.DoAnTotNgiep.Enum.GradingMode;
import com.TrungHieu.DoAnTotNgiep.Enum.SubmittedStatus;
import com.TrungHieu.DoAnTotNgiep.Exception.AppException;
import com.TrungHieu.DoAnTotNgiep.Exception.ErrorCode;
import com.TrungHieu.DoAnTotNgiep.Mapper.StudentSubmissionMapper;
import com.TrungHieu.DoAnTotNgiep.Repository.ClassExamRepository;
import com.TrungHieu.DoAnTotNgiep.Repository.GradingSessionRepository;
import com.TrungHieu.DoAnTotNgiep.Repository.StudentSubmissionRepository;
import com.TrungHieu.DoAnTotNgiep.Repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StudentSubmissionService {
    StudentSubmissionRepository submissionRepository;
    UserRepository userRepository;
    ClassExamRepository classExamRepository;
    StudentSubmissionMapper studentSubmissionMapper;
    GradingSessionRepository sessionRepository;

    public ApiResponse<StudentSubmissionResponse> createSubmission(CreationSubmissionRequest request) {

        User student = userRepository.findById(request.getIdStudent())
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));
        ClassExam classExam = classExamRepository.findById(request.getIdClassExam())
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));

        StudentSubmission studentSubmission = submissionRepository
                .findByStudentAndClassExam(student, classExam)
                .orElse(new StudentSubmission());


        studentSubmission.setStudent(student);
        studentSubmission.setClassExam(classExam);
        studentSubmission.setVideoUrl1(request.getVideoUrl1());
        studentSubmission.setFileSizeBytes1(request.getFileSizeBytes1());
        studentSubmission.setVideoUrl2(request.getVideoUrl2());
        studentSubmission.setFileSizeBytes2(request.getFileSizeBytes2());
        studentSubmission.setStudentData(request.getStudentData());
        studentSubmission.setStatus(SubmittedStatus.SUBMITTED);


        studentSubmission.setSubmittedAt(LocalDateTime.now());

        // 6. Lưu vào DB (Hibernate tự hiểu: có ID thì UPDATE, chưa có ID thì INSERT)
        submissionRepository.save(studentSubmission);
        StudentSubmissionResponse response = studentSubmissionMapper.toResponse(studentSubmission);
        String standardData = classExam.getExam().getStandardData();
        response.setStandardData(standardData);

        return new ApiResponse<>(201, "Nộp bài thành công", response);
    }

    public ApiResponse<List<SubmissionSummaryResponse>> getClassSubmissionSummary(int classExamId) {

        List<StudentSubmission> submissions = submissionRepository.findByClassExamId(classExamId);

        List<SubmissionSummaryResponse> data = submissions.stream().map(sub -> SubmissionSummaryResponse.builder()
                .studentId(sub.getStudent().getId())
                .fullName(sub.getStudent().getFullName())
                .status(sub.getStatus().name())
                .submittedAt(sub.getSubmittedAt())
                .submissionId(sub.getId())
                .build()
        ).collect(Collectors.toList());

        // 3. Trả về ApiResponse với code thành công
        return ApiResponse.<List<SubmissionSummaryResponse>>builder()
                .code(1000)
                .message("Lấy danh sách bài nộp thành công")
                .data(data)
                .build();
    }

    public ApiResponse<List<StudentSubmissionResponse>> getExamStudent(String idStudent){

        User user = userRepository.findById(idStudent).orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));
        List<StudentSubmission> studentSubmissions = user.getStudentSubmissions();
        return new ApiResponse<>(200, "Lấy danh sách bài nộp thành công", studentSubmissionMapper.toResponseList(studentSubmissions));
    }

    public ApiResponse<SubmissionGradeDTO> getGradeInfo(Integer subId) {
        StudentSubmission sub = submissionRepository.findById(subId).orElseThrow();

        // Tìm 2 phiên mới nhất
        GradingSession practice = sessionRepository
                .findFirstBySubmissionIdAndGradingModeOrderByIdDesc(subId, GradingMode.PRACTICE).orElse(null);
        GradingSession official = sessionRepository
                .findFirstBySubmissionIdAndGradingModeOrderByIdDesc(subId, GradingMode.OFFICIAL).orElse(null);

        // MapStruct sẽ tự "nhặt" các trường từ 3 object này vứt vào DTO
        return new ApiResponse<>(200, "Lấy thông tin thành công", studentSubmissionMapper.toGradeDTO(sub, practice, official));
    }

    public ApiResponse<List<SubmissionGradeDTO>> getGradeBoard(Integer classExamId) {
        // Query 1 lần duy nhất lấy toàn bộ danh sách điểm của lớp
        List<SubmissionGradeDTO> gradeBoard = submissionRepository.getGradeBoardByClassExamId(classExamId);

        return ApiResponse.<List<SubmissionGradeDTO>>builder()
                .code(200)
                .message("Lấy bảng điểm lớp " + classExamId + " thành công")
                .data(gradeBoard)
                .build();
    }

    public ApiResponse<List<StudentGradeBoardResponse>> getFullGradeBoard(String classId) {
        // 1. Lấy toàn bộ bản ghi (ví dụ 1 lớp 50 người, mỗi người 4 bài = 200 dòng)
        List<SubmissionGradeDTO> rawData = submissionRepository.getGradeBoardByClassId(classId);

        // 2. Gom nhóm theo StudentCode bằng Stream API
        List<StudentGradeBoardResponse> formattedData = rawData.stream()
                .collect(Collectors.groupingBy(dto -> dto.getStudentCode()))
                .entrySet().stream()
                .map(entry -> {
                    SubmissionGradeDTO firstMatch = entry.getValue().get(0);
                    return StudentGradeBoardResponse.builder()
                            .studentId(firstMatch.getStudentCode()) // Hoặc StudentId nếu sếp có
                            .studentName(firstMatch.getStudentName())
                            .studentCode(entry.getKey())
                            .exams(entry.getValue()) // Đây là danh sách 4 bài thi (Bắn súng, Ném lựu...)
                            .build();
                })
                .collect(Collectors.toList());

        return ApiResponse.<List<StudentGradeBoardResponse>>builder()
                .code(200)
                .message("Lấy danh sách thành công")
                .data(formattedData)
                .build();

    }

    public ApiResponse<Page<SubmissionGradeDTO>> getMyExams(String studentId, int page, int size) {
        // 1. Kiểm tra ID sinh viên (Dùng sếp Hiếu nhé)
        if (!userRepository.existsById(studentId)) {
            throw new AppException(ErrorCode.DATA_NOT_FOUND);
        }

        // 2. Tạo Pageable - Sếp nên để mặc định sắp xếp theo ngày nộp mới nhất lên đầu
        Pageable pageable = PageRequest.of(page, size, Sort.by("submittedAt").descending());

        // 3. Gọi Repository (Hàm này sếp đã nâng cấp lên Page ở bước trước rồi)
        Page<SubmissionGradeDTO> myExams = submissionRepository.getMyGradeBoardByStringId(studentId, pageable);

        return ApiResponse.<Page<SubmissionGradeDTO>>builder()
                .code(200)
                .message("Lấy danh sách bài thi thành công")
                .data(myExams)
                .build();
    }

    public ApiResponse<StudentSubmissionResponse> uploadStudentVideo(CreationSubmissionRequest request) {
        StudentSubmission submission = submissionRepository
                .findByClassExamIdAndStudentId(request.getIdClassExam(), request.getIdStudent())
                .orElseGet(() -> {
                    StudentSubmission newSub = new StudentSubmission();
                    ClassExam classExam = classExamRepository.findById(request.getIdClassExam()).orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));
                    newSub.setClassExam(classExam);
                    User student = userRepository.findById(request.getIdStudent()).orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));
                    newSub.setStudent(student);
                    newSub.setStatus(SubmittedStatus.SUBMITTED);
                    newSub.setSubmittedAt(LocalDateTime.now());
                    newSub.setStudentData(request.getStudentData());
                    return newSub;
                });

        if (request.getVideoUrl1() != null) {
            submission.setVideoUrl1(request.getVideoUrl1());
            submission.setFileSizeBytes1(request.getFileSizeBytes1());
        }
        if (request.getVideoUrl2() != null) {
            submission.setVideoUrl2(request.getVideoUrl2());
            submission.setFileSizeBytes2(request.getFileSizeBytes2());
        }


        StudentSubmission saved = submissionRepository.save(submission);
        return new ApiResponse<>(200, "Nop bai thi thanh cong" ,studentSubmissionMapper.toResponse(saved));

    }
}

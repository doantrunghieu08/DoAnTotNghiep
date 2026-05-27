package com.TrungHieu.DoAnTotNgiep.Service;

import com.TrungHieu.DoAnTotNgiep.DTO.Request.AIRequest.AiGradingRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Request.ErrorRequest.ErrorCriteriaDto;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.AiGradeResponse;
import com.TrungHieu.DoAnTotNgiep.Entity.ClassExam;
import com.TrungHieu.DoAnTotNgiep.Entity.ErrorType;
import com.TrungHieu.DoAnTotNgiep.Entity.User;
import com.TrungHieu.DoAnTotNgiep.Exception.AppException;
import com.TrungHieu.DoAnTotNgiep.Exception.ErrorCode;
import com.TrungHieu.DoAnTotNgiep.Repository.ClassExamRepository;
import com.TrungHieu.DoAnTotNgiep.Repository.ErrorTypeRepository;
import com.TrungHieu.DoAnTotNgiep.Repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AiIntegrationService {
    String AI_SERVICE_URL = "https://stung-ceremony-charity.ngrok-free.dev";
    ErrorTypeRepository errorTypeRepository;
    UserRepository userRepository;
    ClassExamRepository classExamRepository;

    public AiGradeResponse callPythonAiService(
            String idTeacher,
            int idClassExam,
            String submissionVideoUrl) {

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(15000); // 15 giây
        requestFactory.setReadTimeout(300000);   // 5 phút

        RestTemplate restTemplate = new RestTemplate(requestFactory);

        try {
            // 1. Tìm giảng viên
            User teacher = userRepository.findById(idTeacher)
                    .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));

            // 2. Tìm bài thi của lớp (ClassExam)
            ClassExam classExam = classExamRepository.findById(idClassExam)
                    .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));

            // -----------------------------------------------------------------
            // TỐI ƯU TẠI ĐÂY: Lấy chuỗi dữ liệu JSON mẫu từ DB ra thay vì lấy link video
            // -----------------------------------------------------------------
            String standardData = classExam.getExam().getStandardData();

            if (standardData == null || standardData.isBlank()) {
                // Nếu bài thi chưa có dữ liệu AI gốc, báo lỗi dữ liệu chưa sẵn sàng
                throw new AppException(ErrorCode.DATA_NOT_FOUND);
            }

            // 3. Lấy danh sách tiêu chí lỗi của giảng viên
            List<ErrorType> errorTypes = errorTypeRepository.findAllByTeacher(teacher);

            List<ErrorCriteriaDto> allowedErrors = errorTypes.stream()
                    .map(errorType -> ErrorCriteriaDto.builder()
                            .idError(errorType.getId())
                            .errorName(errorType.getDescription())
                            .deduction(errorType.getDeduction())
                            .build())
                    .toList();

            // 4. Đóng gói Payload mới gửi sang Python
            AiGradingRequest requestPayload = AiGradingRequest.builder()
                    .standardData(standardData)          // Thay thế sampleVideoUrl thành dữ liệu cấu trúc mẫu
                    .videoUrl(submissionVideoUrl)        // Link video bài nộp của sinh viên
                    .allowedErrors(allowedErrors)
                    .build();

            URI targetUri = UriComponentsBuilder
                    .fromHttpUrl(AI_SERVICE_URL)
                    .path("/api/ai/grade-video")
                    .build()
                    .toUri();

            // 5. Bắn request sang Python và chờ nhận kết quả chấm bài (Điểm số, List lỗi cụ thể, Student Data JSON)
            return restTemplate.postForObject(
                    targetUri,
                    requestPayload,
                    AiGradeResponse.class
            );

        } catch (HttpStatusCodeException e) {
            System.out.println("AI Service Response Error:");
            System.out.println(e.getResponseBodyAsString());
            throw new AppException(ErrorCode.AI_SERVICE_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AppException(ErrorCode.AI_SERVICE_ERROR);
        }
    }
}
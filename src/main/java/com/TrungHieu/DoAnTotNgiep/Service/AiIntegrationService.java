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
import java.util.ArrayList;
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

        SimpleClientHttpRequestFactory requestFactory =
                new SimpleClientHttpRequestFactory();

        requestFactory.setConnectTimeout(15000); // 15 giây
        requestFactory.setReadTimeout(300000);   // 5 phút

        RestTemplate restTemplate =
                new RestTemplate(requestFactory);

        try {

            // Tìm giảng viên
            User teacher = userRepository.findById(idTeacher)
                    .orElseThrow(() ->
                            new AppException(ErrorCode.DATA_NOT_FOUND));

            // Tìm bài thi
            ClassExam classExam = classExamRepository.findById(idClassExam)
                    .orElseThrow(() ->
                            new AppException(ErrorCode.DATA_NOT_FOUND));

            String sampleVideoUrl =
                    classExam.getExam().getSampleVideoUrl();

            if (sampleVideoUrl == null || sampleVideoUrl.isBlank()) {
                throw new AppException(ErrorCode.DATA_NOT_FOUND);
            }

            // Lấy danh sách lỗi của giảng viên
            List<ErrorType> errorTypes =
                    errorTypeRepository.findAllByTeacher(teacher);

            List<ErrorCriteriaDto> allowedErrors =
                    errorTypes.stream()
                            .map(errorType -> ErrorCriteriaDto.builder()
                                    .idError(errorType.getId())
                                    .errorName(errorType.getDescription())
                                    .deduction(errorType.getDeduction())
                                    .build())
                            .toList();

            // Payload gửi sang Python
            AiGradingRequest requestPayload =
                    AiGradingRequest.builder()
                            .sampleVideoUrl(sampleVideoUrl)
                            .videoUrl(submissionVideoUrl)
                            .allowedErrors(allowedErrors)
                            .build();

            URI targetUri = UriComponentsBuilder
                    .fromHttpUrl(AI_SERVICE_URL)
                    .path("/api/ai/grade-video")
                    .build()
                    .toUri();

            return restTemplate.postForObject(
                    targetUri,
                    requestPayload,
                    AiGradeResponse.class
            );

        } catch (HttpStatusCodeException e) {

            System.out.println("AI Service Response:");
            System.out.println(e.getResponseBodyAsString());

            throw new AppException(ErrorCode.AI_SERVICE_ERROR);

        } catch (Exception e) {

            e.printStackTrace();

            throw new AppException(ErrorCode.AI_SERVICE_ERROR);
        }
    }
}

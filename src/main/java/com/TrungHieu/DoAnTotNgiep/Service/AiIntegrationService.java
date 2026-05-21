package com.TrungHieu.DoAnTotNgiep.Service;

import com.TrungHieu.DoAnTotNgiep.DTO.Request.AIRequest.AiGradingRequest;
import com.TrungHieu.DoAnTotNgiep.DTO.Request.ErrorRequest.ErrorCriteriaDto;
import com.TrungHieu.DoAnTotNgiep.Entity.ErrorType;
import com.TrungHieu.DoAnTotNgiep.Entity.User;
import com.TrungHieu.DoAnTotNgiep.Exception.AppException;
import com.TrungHieu.DoAnTotNgiep.Exception.ErrorCode;
import com.TrungHieu.DoAnTotNgiep.Repository.ErrorTypeRepository;
import com.TrungHieu.DoAnTotNgiep.Repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AiIntegrationService {
    String AI_SERVICE_URL = "https://stung-ceremony-charity.ngrok-free.dev/api/ai/grade-video";
    ErrorTypeRepository errorTypeRepository;
    UserRepository userRepository;

    public String callPythonAiService(String idTeacher, String videoUrl) {
        // 1. Cấu hình RestTemplate kiên nhẫn chờ AI local xử lý trong 60 giây
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5000);   // Chờ kết nối đến ngrok tối đa 5 giây
        requestFactory.setReadTimeout(60000);     // Chờ AI tính toán và trả kết quả tối đa 60 giây

        RestTemplate restTemplate = new RestTemplate(requestFactory);

        // 2. Tìm giảng viên và lấy danh sách lỗi cấu hình trong DB
        User teacher = userRepository.findById(idTeacher)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_NOT_FOUND));

        List<ErrorType> errorTypes = errorTypeRepository.findAllByTeacher(teacher);

        // 3. Tự khởi tạo danh sách DTO ngay trong hàm, không bắt truyền từ ngoài vào
        List<ErrorCriteriaDto> allowedErrors = new ArrayList<>();
        for (ErrorType errorType : errorTypes){
            ErrorCriteriaDto dto = new ErrorCriteriaDto();
            dto.setIdError(errorType.getId());
            dto.setErrorName(errorType.getName());
            dto.setDeduction(errorType.getDeduction());
            allowedErrors.add(dto);
        }

        // 4. Đóng gói dữ liệu bài làm và danh sách lỗi từ DB
        AiGradingRequest requestPayload = new AiGradingRequest(videoUrl, allowedErrors);

        try {
            // 5. Bắn request sang ngrok, nhận về chuỗi JSON kết quả từ Python
            return restTemplate.postForObject(AI_SERVICE_URL, requestPayload, String.class);
        } catch (Exception e) {
            // Ghi log lỗi nếu không kết nối được ngrok hoặc AI bị sập
            e.printStackTrace();
            throw new AppException(ErrorCode.AI_SERVICE_ERROR); // Hoặc trả về chuỗi báo lỗi tùy bạn cấu hình
        }
    }
}

package com.TrungHieu.DoAnTotNgiep.Service;

import com.TrungHieu.DoAnTotNgiep.Entity.GradingSession;
import com.TrungHieu.DoAnTotNgiep.Enum.GradingSessionStatus;
import com.TrungHieu.DoAnTotNgiep.Repository.GradingSessionRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class AutoGradingFinalizer {

    @Autowired
    private GradingSessionRepository gradingSessionRepository;

    // Chạy vào lúc 0 giờ mỗi ngày, hoặc mỗi tiếng một lần tùy sếp
    // cron = "0 0 * * * *" (Chạy vào đầu mỗi giờ)
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void autoFinalizeExams() {
        log.info("Bắt đầu quét và chốt điểm tự động...");

        LocalDateTime now = LocalDateTime.now();

        // 1. Tìm tất cả các GradingSession chưa chốt (IN_PROGRESS/NOT_GRADED)
        // mà cái ClassExam của nó đã quá hạn chấm (gradingDeadline < now)
        List<GradingSession> expiredSessions = gradingSessionRepository
                .findAllByStatusAndSubmission_ClassExam_GradingDeadlineBefore(
                        GradingSessionStatus.IN_PROGRESS, now
                );

        if (!expiredSessions.isEmpty()) {
            for (GradingSession session : expiredSessions) {
                session.setStatus(GradingSessionStatus.FINALIZED);
                // Có thể thêm log: "Hệ thống tự động chốt do quá hạn"
            }
            gradingSessionRepository.saveAll(expiredSessions);
            log.info("Đã tự động chốt {} phiên chấm.", expiredSessions.size());
        }
    }
}
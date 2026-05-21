package com.TrungHieu.DoAnTotNgiep.Repository;

import com.TrungHieu.DoAnTotNgiep.DTO.Response.SubmissionGradeDTO;
import com.TrungHieu.DoAnTotNgiep.Entity.GradingSession;
import com.TrungHieu.DoAnTotNgiep.Enum.GradingMode;
import com.TrungHieu.DoAnTotNgiep.Enum.GradingSessionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface GradingSessionRepository extends JpaRepository<GradingSession, Integer> {
    Optional<GradingSession> findFirstBySubmissionIdAndTeacherIdAndStatusAndGradingMode(
            Integer submissionId,
            String teacherId,
            GradingSessionStatus status,
            GradingMode gradingMode
    );

    Optional<GradingSession> findByIdAndGradingMode(Integer idSession, GradingMode gradingMode);

    Optional<GradingSession> findFirstBySubmissionIdAndGradingModeOrderByIdDesc(
            Integer submissionId,
            GradingMode gradingMode
    );


    List<GradingSession> findByTeacher_IdOrderByIdDesc(String teacherId);

    // Fetch join tránh N+1 — dùng trong service
    @Query("""
    SELECT gs FROM GradingSession gs
    LEFT JOIN FETCH gs.submission sub
    LEFT JOIN FETCH sub.student s
    LEFT JOIN FETCH sub.classExam ce
    LEFT JOIN FETCH ce.examType et
    LEFT JOIN FETCH ce.classes c
    WHERE gs.teacher.id = :teacherId
    ORDER BY gs.id DESC
""")
    List<GradingSession> findDetailsByTeacherId(@Param("teacherId") String teacherId);

    List<GradingSession> findAllByStatusAndSubmission_ClassExam_GradingDeadlineBefore(
            GradingSessionStatus status,
            LocalDateTime dateTime
    );

    Page<GradingSession> findAllBySubmissionIdAndStatusIn(
            Integer submissionId,
            List<GradingSessionStatus> statuses,
            Pageable pageable
    );

    /**
     * Lấy phiên chấm mới nhất theo submissionId + gradingMode, loại trừ FINALIZED.
     * Dùng để hiển thị điểm trên card (PRACTICE / OFFICIAL).
     */
    Optional<GradingSession> findFirstBySubmission_IdAndGradingModeAndStatusInOrderByIdDesc(
            Integer submissionId,
            GradingMode gradingMode,
            List<GradingSessionStatus> statuses
    );

    @Query("SELECT gs FROM GradingSession gs " +
            "JOIN FETCH gs.submission s " +
            "JOIN FETCH s.classExam " +
            "WHERE gs.teacher.id = :teacherId")
    Page<GradingSession> findDetailsByTeacherId(@Param("teacherId") String teacherId, Pageable pageable);

}

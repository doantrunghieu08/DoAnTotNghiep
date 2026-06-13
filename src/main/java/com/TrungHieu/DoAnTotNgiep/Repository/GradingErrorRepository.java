package com.TrungHieu.DoAnTotNgiep.Repository;

import com.TrungHieu.DoAnTotNgiep.Entity.GradingError;
import org.mapstruct.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface GradingErrorRepository extends JpaRepository<GradingError, Integer> {
    @Query("SELECT SUM(e.deductionApplied) FROM GradingError e WHERE e.session.id = :sessionId")
    float sumDeductionBySessionId(@Param("sessionId") int sessionId);

    // Lấy tất cả lỗi của một phiên chấm
    List<GradingError> findBySession_Id(Integer sessionId);

    // Bulk fetch cho nhiều session cùng lúc (tránh N+1 query)
    List<GradingError> findBySession_IdIn(List<Integer> sessionIds);

    // Bulk fetch kèm errorType để lấy tên, độ khấu trừ
    @Query("""
    SELECT ge FROM GradingError ge
    LEFT JOIN FETCH ge.errorType et
    WHERE ge.session.id IN :sessionIds
""")
    List<GradingError> findWithErrorTypeBySessionIdIn(@Param("sessionIds") List<Integer> sessionIds);

    @Query(value = "SELECT count(error_type_id) as TongSoLoiMacPhai FROM grading_errors ge JOIN grading_sessions gs ON ge.session_id = gs.id WHERE gs.teacher_id = :idTeacher", nativeQuery = true)
    Long calculateErrorInExam(@Param("idTeacher") String idTeacher);
}

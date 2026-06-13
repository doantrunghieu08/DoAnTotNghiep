package com.TrungHieu.DoAnTotNgiep.Repository;

import com.TrungHieu.DoAnTotNgiep.DTO.Response.ErrorDistributionResponse;
import com.TrungHieu.DoAnTotNgiep.Entity.ErrorType;
import com.TrungHieu.DoAnTotNgiep.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ErrorTypeRepository extends JpaRepository<ErrorType, Integer> {
    Page<ErrorType> findAllByTeacher(User user, Pageable pageable);

    public List<ErrorType> findAllByTeacher(User teacher);

    @Query(value = "SELECT count(id) as TongCacLoi FROM error_types et where et.teacher_id = :idTeacher", nativeQuery = true)
    Long calculateErrorType(@Param("idTeacher") String idTeacher);

    @Query(value = """
        SELECT
            c.id AS classId,
            c.class_name AS className,
            COUNT(ge.id) AS totalErrors
        FROM classes c
        JOIN class_exams ce
            ON ce.class_id = c.id
        JOIN student_submissions ss
            ON ss.class_exam_id = ce.id
        JOIN grading_sessions gs
            ON gs.submission_id = ss.id
        JOIN grading_errors ge
            ON ge.session_id = gs.id
        WHERE c.teacher_id = :idTeacher
        GROUP BY c.id, c.class_name
        ORDER BY totalErrors DESC
        """, nativeQuery = true)
    List<Object[]> getErrorDistributionByClass(
            @Param("idTeacher") String idTeacher);

    @Query(value = """
    SELECT
        et.name,
        COUNT(ge.id) AS totalOccurrences
    FROM grading_errors ge
    JOIN error_types et
        ON ge.error_type_id = et.id
    JOIN grading_sessions gs
        ON ge.session_id = gs.id
    WHERE gs.teacher_id = :teacherId
    GROUP BY et.id, et.name
    ORDER BY totalOccurrences DESC
    LIMIT 5
    """, nativeQuery = true)
    List<Object[]> getTop5CommonErrors(
            @Param("teacherId") String teacherId);
}

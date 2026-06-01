package com.TrungHieu.DoAnTotNgiep.Repository;

import com.TrungHieu.DoAnTotNgiep.Entity.ExamType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ExamTypeRepository extends JpaRepository<ExamType, Integer> {
    @Query(value = "SELECT exam_code, count(*) as SLBaiThi " +
            "FROM exams e JOIN exam_types et ON e.exam_types_id = et.id  " +
            "GROUP BY exam_code", nativeQuery = true)
    List<Map<String, Object>> countExamByExamCode();
}

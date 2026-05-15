package com.TrungHieu.DoAnTotNgiep.Repository;

import com.TrungHieu.DoAnTotNgiep.Entity.ClassExam;
import com.TrungHieu.DoAnTotNgiep.Entity.Classes;
import com.TrungHieu.DoAnTotNgiep.Entity.ExamType;
import com.TrungHieu.DoAnTotNgiep.Entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassExamRepository extends JpaRepository<ClassExam, Integer> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(value = "UPDATE class_exams SET is_deleted = 1 WHERE class_id = :idClass AND exam_type_id = :idExam", nativeQuery = true)
    void deleteExamByClassIdAndExamId(@Param("idClass") String idClass, @Param("idExam") int idExam);

    @Query(value = "SELECT * FROM class_exams WHERE class_id = :#{#classes.id} AND exam_type_id = :#{#examType.id}", nativeQuery = true)
    Optional<ClassExam> findByClassesAndExamType(@Param("classes") Classes classes, @Param("examType") ExamType examType);

    // Lấy danh sách bài thi đang hoạt động (isDeleted = false)
    Page<ClassExam> findByClasses_IdAndIsDeletedFalse(String classId, Pageable pageable);

    // Lấy danh sách bài thi đã xóa (isDeleted = true)
    Page<ClassExam> findByClasses_IdAndIsDeletedTrue(String classId, Pageable pageable);

    // Tìm kiếm để khôi phục
    Optional<ClassExam> findByClasses_IdAndExamType_Id(String classId, int examId);
}

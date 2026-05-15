package com.TrungHieu.DoAnTotNgiep.Repository;

import com.TrungHieu.DoAnTotNgiep.DTO.Response.SubmissionGradeDTO;
import com.TrungHieu.DoAnTotNgiep.Entity.ClassExam;
import com.TrungHieu.DoAnTotNgiep.Entity.StudentSubmission;
import com.TrungHieu.DoAnTotNgiep.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentSubmissionRepository extends JpaRepository<StudentSubmission, Integer> {
    List<StudentSubmission> findByClassExamId(int classExamId);
    Optional<StudentSubmission> findByStudentAndClassExam(User student, ClassExam classExam);

    // 1. Lấy bảng điểm theo ClassExam (Thêm avatarImage)
    @Query("SELECT new com.TrungHieu.DoAnTotNgiep.DTO.Response.SubmissionGradeDTO(" +
            "s.id, u.fullName, u.username, u.avatarImage, " + // Thêm ở vị trí số 4
            "ps.id, ps.finalScore, ps.status, " +
            "os.id, os.finalScore, os.status, " +
            "s.submittedAt) " +
            "FROM StudentSubmission s " +
            "JOIN s.student u " +
            "LEFT JOIN GradingSession ps ON ps.id = (SELECT MAX(g1.id) FROM GradingSession g1 WHERE g1.submission = s AND g1.gradingMode = com.TrungHieu.DoAnTotNgiep.Enum.GradingMode.PRACTICE) " +
            "LEFT JOIN GradingSession os ON os.id = (SELECT MAX(g2.id) FROM GradingSession g2 WHERE g2.submission = s AND g2.gradingMode = com.TrungHieu.DoAnTotNgiep.Enum.GradingMode.OFFICIAL) " +
            "WHERE s.classExam.id = :classExamId")
    List<SubmissionGradeDTO> getGradeBoardByClassExamId(@Param("classExamId") Integer classExamId);

    // 2. Lấy bảng điểm theo Lớp (Thêm avatarImage)
    @Query("SELECT new com.TrungHieu.DoAnTotNgiep.DTO.Response.SubmissionGradeDTO(" +
            "s.id, u.fullName, u.username, u.avatarImage, " +
            "ps.id, ps.finalScore, ps.status, " +
            "os.id, os.finalScore, os.status, " +
            "s.submittedAt) " +
            "FROM StudentSubmission s " +
            "JOIN s.student u " +
            "JOIN s.classExam ce " +
            "LEFT JOIN GradingSession ps ON ps.id = (SELECT MAX(g1.id) FROM GradingSession g1 WHERE g1.submission = s AND g1.gradingMode = com.TrungHieu.DoAnTotNgiep.Enum.GradingMode.PRACTICE) " +
            "LEFT JOIN GradingSession os ON os.id = (SELECT MAX(g2.id) FROM GradingSession g2 WHERE g2.submission = s AND g2.gradingMode = com.TrungHieu.DoAnTotNgiep.Enum.GradingMode.OFFICIAL) " +
            "WHERE ce.classes.id = :classId")
    List<SubmissionGradeDTO> getGradeBoardByClassId(@Param("classId") String classId);

    // 3. Lấy bảng điểm cá nhân - PHÂN TRANG (Thêm avatarImage)
    @Query(value = "SELECT new com.TrungHieu.DoAnTotNgiep.DTO.Response.SubmissionGradeDTO(" +
            "s.id, u.fullName, u.username, u.avatarImage, " + // Đổi u.id thành u.username cho khớp studentCode
            "ps.id, ps.finalScore, ps.status, " +
            "os.id, os.finalScore, os.status, " +
            "s.submittedAt) " +
            "FROM StudentSubmission s " +
            "JOIN s.student u " +
            "LEFT JOIN GradingSession ps ON ps.id = (SELECT MAX(g1.id) FROM GradingSession g1 WHERE g1.submission = s AND g1.gradingMode = com.TrungHieu.DoAnTotNgiep.Enum.GradingMode.PRACTICE) " +
            "LEFT JOIN GradingSession os ON os.id = (SELECT MAX(g2.id) FROM GradingSession g2 WHERE g2.submission = s AND g2.gradingMode = com.TrungHieu.DoAnTotNgiep.Enum.GradingMode.OFFICIAL) " +
            "WHERE u.id = :studentId",
            countQuery = "SELECT COUNT(s) FROM StudentSubmission s WHERE s.student.id = :studentId")
    Page<SubmissionGradeDTO> getMyGradeBoardByStringId(@Param("studentId") String studentId, Pageable pageable);

}

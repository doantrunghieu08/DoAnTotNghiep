package com.TrungHieu.DoAnTotNgiep.Repository;

import com.TrungHieu.DoAnTotNgiep.DTO.Response.StudentResponse;
import com.TrungHieu.DoAnTotNgiep.Entity.Classes;
import com.TrungHieu.DoAnTotNgiep.Entity.User;
import com.TrungHieu.DoAnTotNgiep.Enum.RoleType;
import com.TrungHieu.DoAnTotNgiep.Enum.UserStatusType;
import jakarta.validation.constraints.Past;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
    public Optional<User> findByEmail(String email);

    @Query(value = "SELECT new com.TrungHieu.DoAnTotNgiep.DTO.Response.StudentResponse(" +
            "u.id, u.fullName, u.gender, u.birthday, u.studentClass.id, u.avatarImage) " +
            "FROM User u WHERE u.studentClass.id = :classId AND u.role = 'STUDENT'",
            countQuery = "SELECT COUNT(u) FROM User u WHERE u.studentClass.id = :classId AND u.role = 'STUDENT'")
    Page<StudentResponse> findStudentsByClassId(@Param("classId") String classId, Pageable pageable);

    @Query("SELECT u " +
            "FROM User u WHERE u.studentClass.id = :classId AND u.role = 'STUDENT' AND u.id = :id")
    public Optional<User> findStudentByClassIdAndId(@Param("classId") String classId, @Param("id") String id);


    boolean existsByUsername(String username);

    @Query(value = "SELECT * FROM users WHERE role = :roleType", nativeQuery = true)
    Page<User> findByRoleNative(@Param("roleType") String roleType, Pageable pageable);

    List<User> findAllByRole(RoleType roleType);

    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<User> searchAllUsers(@Param("keyword") String keyword, Pageable pageable);


    @Query("SELECT u FROM User u WHERE " +
            "u.studentClass.id = :classId AND " +
            "u.role = com.TrungHieu.DoAnTotNgiep.Enum.RoleType.STUDENT AND " +
            "(LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<User> searchStudentsInClass(@Param("classId") String classId,
                                     @Param("keyword") String keyword,
                                     Pageable pageable);

    long countByRole(RoleType role);

    long countByStatus(UserStatusType status);

    @Query(value = """
    SELECT COUNT(u.id)
    FROM users u
    JOIN classes c ON u.class_id = c.id
    WHERE u.role = 'STUDENT'
      AND c.teacher_id = :idTeacher
    """, nativeQuery = true)
    Long calculateTheTotalNumberOfStudentByTeacher(
            @Param("idTeacher") String idTeacher);
}

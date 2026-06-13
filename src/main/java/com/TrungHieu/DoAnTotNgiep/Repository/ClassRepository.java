package com.TrungHieu.DoAnTotNgiep.Repository;

import com.TrungHieu.DoAnTotNgiep.Entity.ClassExam;
import com.TrungHieu.DoAnTotNgiep.Entity.Classes;
import com.TrungHieu.DoAnTotNgiep.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRepository extends JpaRepository<Classes, String> {
    @Query("SELECT COUNT(u.id) FROM User u WHERE u.studentClass.id = :id")
    public long calculateTheTotalNumberOfStudents(@Param("id") String id);

    @Query("SELECT c.id, COUNT(u) " +
            "FROM Classes c " +
            "LEFT JOIN User u ON u.studentClass.id = c.id " +
            "GROUP BY c.id")
    List<Object[]> countStudentsByClassId();

    List<Classes> findAllByTeacher(User user);

    Page<Classes> findAllByTeacher(User user, Pageable pageable);

    @Query(value = "SELECT count(c.id) as SLLop\n" +
            "FROM classes c\n" +
            "WHERE c.teacher_id = :idTeacher;", nativeQuery = true)
    Long calculateTheTotalNumberOfClasses(@Param("idTeacher") String idTeacher);

}

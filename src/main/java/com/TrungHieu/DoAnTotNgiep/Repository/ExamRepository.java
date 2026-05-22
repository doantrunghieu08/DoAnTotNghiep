package com.TrungHieu.DoAnTotNgiep.Repository;

import com.TrungHieu.DoAnTotNgiep.Entity.Exam;
import com.TrungHieu.DoAnTotNgiep.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamTypeRepository extends JpaRepository<Exam, Integer> {
    Page<Exam> findAllByTeacher(User user, Pageable pageable);

    public List<Exam> findAllByTeacher(User user);
}

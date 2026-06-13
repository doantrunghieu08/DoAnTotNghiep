package com.TrungHieu.DoAnTotNgiep.Repository;

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
}

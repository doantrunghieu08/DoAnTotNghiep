package com.TrungHieu.DoAnTotNgiep.Entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "class_exams")
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class ClassExam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne
    @JoinColumn(name = "class_id")
    Classes classes;

    @ManyToOne
    @JoinColumn(name = "exam_type_id")
    Exam exam;

    LocalDateTime submissionDeadline;
    LocalDateTime gradingDeadline;


    @Column(name = "is_deleted")
    Boolean isDeleted = false;

    @OneToMany(mappedBy = "classExam")
    List<StudentSubmission> studentSubmissions;

    @CreatedDate // <--- Tự động điền ngày giờ khi tạo mới
    @Column(name = "created_at", updatable = false, nullable = false)
    LocalDateTime createdAt;

    @LastModifiedDate // <--- Tự động cập nhật ngày giờ khi sửa đổi
    @Column(name = "updated_at")
    LocalDateTime updatedAt;

}

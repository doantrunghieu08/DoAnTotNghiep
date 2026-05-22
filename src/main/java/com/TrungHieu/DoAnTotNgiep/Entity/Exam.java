package com.TrungHieu.DoAnTotNgiep.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "exams")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Exam {
    @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String name;

    String description;

    String sampleVideoUrl;

    @Column(name = "is_deleted")
    boolean isDeleted;

    @ManyToOne
            @JoinColumn(name = "teacher_id")
    User teacher;

    @OneToMany(mappedBy = "exam")
    List<ClassExam> classExams;

    @ManyToOne
            @JoinColumn(name = "exam_types_id")
    ExamType examType;

    @CreatedDate // <--- Tự động điền ngày giờ khi tạo mới
    @Column(name = "created_at", updatable = false, nullable = false)
    LocalDateTime createdAt;

    @LastModifiedDate // <--- Tự động cập nhật ngày giờ khi sửa đổi
    @Column(name = "updated_at")
    LocalDateTime updatedAt;

}

package com.TrungHieu.DoAnTotNgiep.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "exam_types")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExamType {
    @Id
    int id;

    String examCode;

    boolean isDeleted;

    @OneToMany(mappedBy = "examType")
    List<Exam> exams;

    @CreatedDate // <--- Tự động điền ngày giờ khi tạo mới
    @Column(name = "created_at", updatable = false, nullable = false)
    LocalDateTime createdAt;

    @LastModifiedDate // <--- Tự động cập nhật ngày giờ khi sửa đổi
    @Column(name = "updated_at")
    LocalDateTime updatedAt;
}

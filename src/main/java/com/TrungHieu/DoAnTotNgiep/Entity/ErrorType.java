package com.TrungHieu.DoAnTotNgiep.Entity;

import com.TrungHieu.DoAnTotNgiep.Enum.SeverityType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "error_types")
@FieldDefaults(level = AccessLevel.PRIVATE)

public class ErrorType {
    @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne
            @JoinColumn(name = "teacher_id")
    User teacher;

    String name;

    String description;

    @Enumerated(value = EnumType.STRING)
    SeverityType severity;

    float deduction;

    boolean isActive;

    @OneToMany(mappedBy = "errorType")
    List<GradingError> gradingErrors;

    @CreatedDate // <--- Tự động điền ngày giờ khi tạo mới
    @Column(name = "created_at", updatable = false, nullable = false)
    LocalDateTime createdAt;

    @LastModifiedDate // <--- Tự động cập nhật ngày giờ khi sửa đổi
    @Column(name = "updated_at")
    LocalDateTime updatedAt;
}

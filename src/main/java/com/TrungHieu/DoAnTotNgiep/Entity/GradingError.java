package com.TrungHieu.DoAnTotNgiep.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "grading_errors")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class GradingError {
    @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne
            @JoinColumn(name = "session_id")
    GradingSession session;

    @ManyToOne
            @JoinColumn(name = "error_type_id")
    ErrorType errorType;

    double frameTimeSeconds;

    String frameImageUrl;

    float deductionApplied;

    String notes;

    @CreatedDate // <--- Tự động điền ngày giờ khi tạo mới
    @Column(name = "created_at", updatable = false, nullable = false)
    LocalDateTime createdAt;

    @LastModifiedDate // <--- Tự động cập nhật ngày giờ khi sửa đổi
    @Column(name = "updated_at")
    LocalDateTime updatedAt;
}

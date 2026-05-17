package com.TrungHieu.DoAnTotNgiep.Entity;

import com.TrungHieu.DoAnTotNgiep.Enum.GradingMode;
import com.TrungHieu.DoAnTotNgiep.Enum.GradingSessionStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "grading_sessions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class GradingSession {
    @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne
            @JoinColumn(name = "submission_id")
    StudentSubmission submission;

    @ManyToOne
            @JoinColumn(name = "teacher_id")
    User teacher;

    @Enumerated(value = EnumType.STRING)
    GradingMode gradingMode;

    float totalDeduction;

    float finalScore;


    @CreatedDate // <--- Tự động cập nhật ngày giờ khi sửa đổi
    @Column(name = "graded_at")
    Instant gradedAt;

    @Enumerated(value = EnumType.STRING)
    GradingSessionStatus status;

    @OneToMany(mappedBy = "session")
    List<GradingError> gradingErrors;

}

package com.TrungHieu.DoAnTotNgiep.Entity;

import com.TrungHieu.DoAnTotNgiep.Enum.SubmittedStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "student_submissions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class StudentSubmission {
    @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne
            @JoinColumn(name = "class_exam_id")
    ClassExam classExam;

    @ManyToOne
            @JoinColumn(name = "student_id")
    User student;

    @Column(name = "video_url_1")
    String videoUrl1;

    @Column(name = "file_size_bytes_1")
    long fileSizeBytes1;

    @Column(name = "video_url_2")
    String videoUrl2;

    @Column(name = "file_size_bytes_2")
    long fileSizeBytes2;

    @CreatedDate // <--- Tự động điền ngày giờ khi tạo mới
    @Column(name = "submitted_at", updatable = false, nullable = false)
    LocalDateTime submittedAt;

    @Enumerated(value = EnumType.STRING)
    SubmittedStatus status;
}

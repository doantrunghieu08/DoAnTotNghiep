package com.TrungHieu.DoAnTotNgiep.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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

    @OneToMany(mappedBy = "examType")
    List<ClassExam> classExams;

    @ManyToOne
            @JoinColumn(name = "exam_types_id")
    ExamType examType;

}

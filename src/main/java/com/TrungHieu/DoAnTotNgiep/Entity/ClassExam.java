package com.TrungHieu.DoAnTotNgiep.Entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "class_exams")
@FieldDefaults(level = AccessLevel.PRIVATE)

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

}

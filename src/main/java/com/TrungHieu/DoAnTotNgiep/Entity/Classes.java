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
@Table(name = "classes")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Classes {
    @Id
    String id;

    String className;

    String semester;

    String academicYear;

    String roomNumber;

    @ManyToOne
            @JoinColumn(name = "teacher_id")
    User teacher;

    @OneToMany(mappedBy = "classes")
    List<ClassExam> classExams;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;



}

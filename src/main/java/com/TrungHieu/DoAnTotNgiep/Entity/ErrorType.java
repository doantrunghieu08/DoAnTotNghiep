package com.TrungHieu.DoAnTotNgiep.Entity;

import com.TrungHieu.DoAnTotNgiep.Enum.SeverityType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Where;

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
}

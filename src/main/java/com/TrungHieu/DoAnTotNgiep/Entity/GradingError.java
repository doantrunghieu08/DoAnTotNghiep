package com.TrungHieu.DoAnTotNgiep.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Table(name = "grading_errors")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
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

}

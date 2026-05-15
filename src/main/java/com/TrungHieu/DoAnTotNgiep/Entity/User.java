package com.TrungHieu.DoAnTotNgiep.Entity;

import com.TrungHieu.DoAnTotNgiep.Enum.GenderType;
import com.TrungHieu.DoAnTotNgiep.Enum.RoleType;
import com.TrungHieu.DoAnTotNgiep.Enum.UserStatusType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    String id;

    String fullName;

    @Column(unique = true, nullable = false)
    String username;

    @Column(name = "password_hash")
    String password;

    @Enumerated(value = EnumType.STRING)
    RoleType role;


    LocalDate birthday;

    String email;

    @Enumerated(value = EnumType.STRING)
    GenderType gender;

    @Enumerated(value = EnumType.STRING)
    UserStatusType status;

    String avatarImage;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "class_id")
    Classes studentClass;

    @OneToMany(mappedBy = "teacher")
    List<Classes> classes;

    @OneToMany(mappedBy = "teacher")
    List<ExamType> examTypes;

    @OneToMany(mappedBy = "teacher")
    List<ErrorType> errorTypes;

    Boolean isRequirePasswordChange;

    @OneToMany(mappedBy = "student")
    List<StudentSubmission> studentSubmissions;
}

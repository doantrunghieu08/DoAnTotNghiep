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
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
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

    @CreatedDate // <--- Tự động điền ngày giờ khi tạo mới
    @Column(name = "created_at", updatable = false, nullable = false)
    LocalDateTime createdAt;

    @LastModifiedDate // <--- Tự động cập nhật ngày giờ khi sửa đổi
    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "class_id")
    Classes studentClass;

    @OneToMany(mappedBy = "teacher")
    List<Classes> classes;

    @OneToMany(mappedBy = "teacher")
    List<Exam> exams;

    @OneToMany(mappedBy = "teacher")
    List<ErrorType> errorTypes;

    Boolean isRequirePasswordChange;

    @OneToMany(mappedBy = "student")
    List<StudentSubmission> studentSubmissions;
}

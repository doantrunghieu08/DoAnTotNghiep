package com.TrungHieu.DoAnTotNgiep.DTO.Request.AuthRequest;

import com.TrungHieu.DoAnTotNgiep.Enum.GenderType;
import com.TrungHieu.DoAnTotNgiep.Enum.RoleType;
import com.TrungHieu.DoAnTotNgiep.Enum.UserStatusType;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequest {
    @NonNull
    String id;

    @NonNull
    String fullName;

    @NonNull
    String username;

    @NonNull
    @Length(min = 6)
    String password;

    @NonNull
    RoleType role;


    @Past
    LocalDate birthday;

    @Email
    String email;

    @NonNull
    GenderType gender;

    @NonNull
    UserStatusType statusType;

    String avatarImage;
}

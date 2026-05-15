package com.TrungHieu.DoAnTotNgiep.DTO.Request.UserRequest;

import com.TrungHieu.DoAnTotNgiep.Enum.GenderType;
import com.TrungHieu.DoAnTotNgiep.Enum.RoleType;
import com.TrungHieu.DoAnTotNgiep.Enum.UserStatusType;
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
public class UpdateUserRequest {
    @NonNull
    String fullName;

    String username;

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

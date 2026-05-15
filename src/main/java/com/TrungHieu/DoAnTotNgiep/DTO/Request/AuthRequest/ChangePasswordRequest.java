package com.TrungHieu.DoAnTotNgiep.DTO.Request.AuthRequest;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Value;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordRequest {
    @NonNull
    String username;

    @NonNull
    String oldPassword;

    @NonNull
    @Length(min = 6)
    String newPassword;
}

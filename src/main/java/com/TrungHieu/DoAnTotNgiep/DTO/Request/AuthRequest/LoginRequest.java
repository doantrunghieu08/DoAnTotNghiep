package com.TrungHieu.DoAnTotNgiep.DTO.Request.AuthRequest;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequest {
    @NonNull
    String username;

    @NonNull
    @Length(min = 6)
    String password;
}

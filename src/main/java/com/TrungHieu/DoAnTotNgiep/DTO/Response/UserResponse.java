package com.TrungHieu.DoAnTotNgiep.DTO.Response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.GeneratedValue;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    String id;
    String fullName;
    String username;
    Date birthday;
    String email;
    String gender;
    String role;
    String status;
    String token;
    String refreshToken;
    boolean isRequirePasswordChange;
    String idClass;
    String avatarImage;
}

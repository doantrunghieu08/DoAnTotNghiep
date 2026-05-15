package com.TrungHieu.DoAnTotNgiep.DTO.Response;

import com.TrungHieu.DoAnTotNgiep.Enum.GenderType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentResponse {
    String id;
    String fullName;
    GenderType gender;
    LocalDate birthday;
    String idClass;
    String avatarImage;
}

package com.TrungHieu.DoAnTotNgiep.DTO.Request.UserRequest;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RemoveStudentFromClassRequest {
    String idStudent;
    String idClass;
}

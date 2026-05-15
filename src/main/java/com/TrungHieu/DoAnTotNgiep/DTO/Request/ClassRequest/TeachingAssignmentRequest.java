package com.TrungHieu.DoAnTotNgiep.DTO.Request.ClassRequest;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TeachingAssignmentRequest {
    String idClass;
    String idTeacher;
}

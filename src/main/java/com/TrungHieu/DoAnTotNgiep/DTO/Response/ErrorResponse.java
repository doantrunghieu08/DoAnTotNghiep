package com.TrungHieu.DoAnTotNgiep.DTO.Response;

import com.TrungHieu.DoAnTotNgiep.Enum.SeverityType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorResponse {
    int id;
    String idTeacher;
    String name;
    String description;
    SeverityType severityType;
    float deduction;
    Boolean isActive;
}

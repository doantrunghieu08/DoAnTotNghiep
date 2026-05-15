package com.TrungHieu.DoAnTotNgiep.DTO.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClassResponse {
    String id;
    String className;
    String semester;
    String academicYear;
    String roomNumber;
    String teacherName;
    long totalStudent;
}

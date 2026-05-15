package com.TrungHieu.DoAnTotNgiep.DTO.Request.ClassRequest;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreationClassRequest {
    @NonNull
    String id;

    @NonNull
    String className;

    @NonNull
    String semester;

    @NonNull
    String academicYear;

    @NonNull
    String roomNumber;

    String teacherId;
}

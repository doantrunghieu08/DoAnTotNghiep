package com.TrungHieu.DoAnTotNgiep.DTO.Request.AuthRequest;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchingStudentRequest {
    @NonNull
    String id;
}

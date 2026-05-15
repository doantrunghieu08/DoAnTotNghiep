package com.TrungHieu.DoAnTotNgiep.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDashboardResponse {
    long totalUsers;
    long totalTeachers;
    long totalStudents;
    long lockedUsers;
}

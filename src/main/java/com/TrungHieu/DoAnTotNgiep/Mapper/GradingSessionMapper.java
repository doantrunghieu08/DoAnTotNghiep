package com.TrungHieu.DoAnTotNgiep.Mapper;

import com.TrungHieu.DoAnTotNgiep.DTO.Response.GradingSessionResponse;
import com.TrungHieu.DoAnTotNgiep.Entity.GradingSession;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {StudentSubmissionMapper.class, UserMapper.class}
)
public interface GradingSessionMapper {
    @Mapping(source = "submission", target = "studentSubmissionResponse")
    @Mapping(source = "teacher", target = "userResponse")
    @Mapping(source = "finalScore", target = "finalScore")
    GradingSessionResponse toResponse(GradingSession gradingSession);
}

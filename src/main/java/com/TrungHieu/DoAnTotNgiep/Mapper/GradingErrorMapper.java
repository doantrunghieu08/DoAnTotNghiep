package com.TrungHieu.DoAnTotNgiep.Mapper;

import com.TrungHieu.DoAnTotNgiep.DTO.Response.GradingErrorResponse;
import com.TrungHieu.DoAnTotNgiep.Entity.GradingError;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GradingErrorMapper {

    @Mapping(target = "errorName", source = "errorType.name")
    @Mapping(target = "errorDescription", source = "errorType.description")
    @Mapping(target = "deduction", source = "errorType.deduction")
    GradingErrorResponse toResponse(GradingError gradingError);

    List<GradingErrorResponse> toResponseList(List<GradingError> gradingErrors);
}

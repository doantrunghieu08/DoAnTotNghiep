package com.TrungHieu.DoAnTotNgiep.Mapper;

import com.TrungHieu.DoAnTotNgiep.DTO.Response.ErrorResponse;
import com.TrungHieu.DoAnTotNgiep.Entity.ErrorType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ErrorMapper {
    @Mapping(target = "idTeacher", source = "teacher.id")
    @Mapping(target = "severityType", source = "severity")
    @Mapping(target = "isActive", source = "active")
    public ErrorResponse toResponse(ErrorType errorType);

    public List<ErrorResponse> toResponseList(List<ErrorType> errorTypes);
}

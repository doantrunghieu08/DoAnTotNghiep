package com.TrungHieu.DoAnTotNgiep.Mapper;

import com.TrungHieu.DoAnTotNgiep.DTO.Response.ClassResponse;
import com.TrungHieu.DoAnTotNgiep.Entity.Classes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClassMapper {
    @Mapping(source = "classes.teacher.fullName", target = "teacherName")

    public ClassResponse toResponse(Classes classes);

    public List<ClassResponse> toResponseList(List<Classes> classes);
}

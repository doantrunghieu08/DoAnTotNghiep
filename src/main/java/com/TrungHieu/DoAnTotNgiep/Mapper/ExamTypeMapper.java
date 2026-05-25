package com.TrungHieu.DoAnTotNgiep.Mapper;

import com.TrungHieu.DoAnTotNgiep.DTO.Response.ExamCodeResponse;
import com.TrungHieu.DoAnTotNgiep.Entity.ExamType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExamTypeMapper {
    @Mapping(target = "examCode", source = "examCode")
    public ExamCodeResponse toResponse(ExamType examType);

    public List<ExamCodeResponse> toResponseList(List<ExamType> examTypes);
}

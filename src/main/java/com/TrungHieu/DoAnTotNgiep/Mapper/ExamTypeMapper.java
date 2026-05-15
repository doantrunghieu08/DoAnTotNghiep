package com.TrungHieu.DoAnTotNgiep.Mapper;

import com.TrungHieu.DoAnTotNgiep.DTO.Response.ExamResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.ExamTypeResponse;
import com.TrungHieu.DoAnTotNgiep.Entity.ExamType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExamTypeMapper {
    @Mapping(target = "isDeleted", source = "deleted")
    public ExamTypeResponse toResponse(ExamType examType);

    public List<ExamTypeResponse> toResponseList(List<ExamType> examTypes);


}

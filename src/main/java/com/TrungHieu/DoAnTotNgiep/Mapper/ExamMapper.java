package com.TrungHieu.DoAnTotNgiep.Mapper;

import com.TrungHieu.DoAnTotNgiep.DTO.Response.ExamTypeResponse;
import com.TrungHieu.DoAnTotNgiep.Entity.Exam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExamMapper {
    @Mapping(target = "isDeleted", source = "deleted")
    @Mapping(target = "examCode", source = "examType.examCode")
    public ExamTypeResponse toResponse(Exam exam);

    public List<ExamTypeResponse> toResponseList(List<Exam> exams);


}

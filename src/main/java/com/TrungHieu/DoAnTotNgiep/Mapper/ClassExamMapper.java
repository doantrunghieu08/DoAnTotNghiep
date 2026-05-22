package com.TrungHieu.DoAnTotNgiep.Mapper;

import com.TrungHieu.DoAnTotNgiep.DTO.Response.ClassExamResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.ExamResponse;
import com.TrungHieu.DoAnTotNgiep.Entity.ClassExam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ExamMapper.class})
public interface ClassExamMapper {
    @Mapping(target = "idClass", source = "classes.id")
    @Mapping(target = "idExam", source = "exam.id")
    public ClassExamResponse toResponse(ClassExam classExam);

    @Mapping(target = "idClass", source = "classes.id")

    @Mapping(target = "examTypeResponse", source = "exam")

    @Mapping(target = "submissionDeadline", source = "submissionDeadline")

    @Mapping(target = "gradingDeadline", source = "gradingDeadline")
    ExamResponse toExamResponse(ClassExam classExam);
}

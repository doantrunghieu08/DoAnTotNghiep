package com.TrungHieu.DoAnTotNgiep.Mapper;

import com.TrungHieu.DoAnTotNgiep.DTO.Response.ClassExamResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.ClassResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.ExamResponse;
import com.TrungHieu.DoAnTotNgiep.Entity.ClassExam;
import com.TrungHieu.DoAnTotNgiep.Entity.ExamType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.lang.annotation.Target;

@Mapper(componentModel = "spring", uses = {ExamTypeMapper.class})
public interface ClassExamMapper {
    @Mapping(target = "idClass", source = "classes.id")
    @Mapping(target = "idExam", source = "examType.id")
    public ClassExamResponse toResponse(ClassExam classExam);

    @Mapping(target = "idClass", source = "classes.id")

    @Mapping(target = "examTypeResponse", source = "examType")

    @Mapping(target = "submissionDeadline", source = "submissionDeadline")

    @Mapping(target = "gradingDeadline", source = "gradingDeadline")
    ExamResponse toExamResponse(ClassExam classExam);
}

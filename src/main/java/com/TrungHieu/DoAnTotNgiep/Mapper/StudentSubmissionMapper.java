package com.TrungHieu.DoAnTotNgiep.Mapper;

import com.TrungHieu.DoAnTotNgiep.DTO.Response.StudentSubmissionResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.SubmissionGradeDTO;
import com.TrungHieu.DoAnTotNgiep.Entity.GradingSession;
import com.TrungHieu.DoAnTotNgiep.Entity.StudentSubmission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentSubmissionMapper {
    @Mapping(target = "idClassExam", source = "classExam.id")
    @Mapping(target = "idStudent", source = "student.id")
    public StudentSubmissionResponse toResponse(StudentSubmission studentSubmission);

    public List<StudentSubmissionResponse> toResponseList(List<StudentSubmission> studentSubmission);



    @Mapping(target = "submissionId", source = "sub.id")
    @Mapping(target = "studentName", source = "sub.student.fullName")
    @Mapping(target = "studentCode", source = "sub.student.id")
    @Mapping(target = "lastUpdated", source = "sub.submittedAt")

    // Ánh xạ cho phần Practice
    @Mapping(target = "practiceSessionId", source = "practice.id")
    @Mapping(target = "practiceScore", source = "practice.finalScore")
    @Mapping(target = "practiceStatus", source = "practice.status")

    // Ánh xạ cho phần Official
    @Mapping(target = "officialSessionId", source = "official.id")
    @Mapping(target = "officialScore", source = "official.finalScore")
    @Mapping(target = "officialStatus", source = "official.status")
    SubmissionGradeDTO toGradeDTO(StudentSubmission sub, GradingSession practice, GradingSession official);

    // Mẹo nhỏ: MapStruct sẽ tự hiểu nếu practice hoặc official là null thì các field liên quan sẽ null.
}

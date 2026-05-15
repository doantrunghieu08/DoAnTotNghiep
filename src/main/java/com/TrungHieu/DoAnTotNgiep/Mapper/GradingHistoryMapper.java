package com.TrungHieu.DoAnTotNgiep.Mapper;

import com.TrungHieu.DoAnTotNgiep.DTO.Response.GradingFrameResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.GradingHistoryResponse;
import com.TrungHieu.DoAnTotNgiep.Entity.GradingError;
import com.TrungHieu.DoAnTotNgiep.Entity.GradingSession;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GradingHistoryMapper {

    @Mapping(target = "id",
            expression = "java(String.valueOf(session.getId()))")
    @Mapping(target = "gradingSessionId",
            expression = "java(String.valueOf(session.getId()))")
    @Mapping(target = "submissionId",
            expression = "java(session.getSubmission() != null ? String.valueOf(session.getSubmission().getId()) : null)")
    @Mapping(target = "examId",
            expression = "java(session.getSubmission() != null && session.getSubmission().getClassExam() != null && session.getSubmission().getClassExam().getExamType() != null ? String.valueOf(session.getSubmission().getClassExam().getExamType().getId()) : null)")
    @Mapping(source = "teacher.id",                                  target = "teacherId")
    @Mapping(source = "teacher.fullName",                            target = "teacherName")
    @Mapping(source = "submission.student.id",                       target = "studentCode")
    @Mapping(source = "submission.student.fullName",                 target = "studentName")
    @Mapping(source = "submission.classExam.classes.id",             target = "classId")
    @Mapping(source = "submission.classExam.classes.className",      target = "className")
    @Mapping(source = "submission.classExam.examType.name",          target = "examName")
    @Mapping(source = "gradingMode",                                 target = "gradingMode")
    @Mapping(source = "totalDeduction",                              target = "totalDeductions")
    @Mapping(source = "finalScore",                                  target = "finalScore")
    @Mapping(source = "gradedAt", target = "gradedAt")
    @Mapping(target = "totalFrames",                                 ignore = true)
    @Mapping(target = "frames",                                      ignore = true)
    GradingHistoryResponse toResponse(GradingSession session);

    @Mapping(source = "id",                  target = "errorId")
    @Mapping(source = "errorType.id",        target = "errorTypeId")
    @Mapping(source = "errorType.name",      target = "errorTypeName")
    @Mapping(source = "errorType.severity",  target = "severity")
    @Mapping(source = "deductionApplied",    target = "deductionApplied")
    @Mapping(source = "notes",               target = "notes")
    GradingFrameResponse toFrameResponse(GradingError error);
}
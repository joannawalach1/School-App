package com.schoolworld.SchoolApp.mappers;

import com.schoolworld.SchoolApp.domain.Exam;
import com.schoolworld.SchoolApp.domain.dto.ExamDto;
import com.schoolworld.SchoolApp.repository.ExamRepo;
import com.schoolworld.SchoolApp.repository.StudentRepo;
import com.schoolworld.SchoolApp.repository.SubjectRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExamMapper {
    private final ExamRepo examRepo;
    private final SubjectRepo subjectRepo;
    private final StudentRepo studentRepo;

    public Exam toEntity(ExamDto examDto) {
        Exam exam = new Exam();
        exam.setNameOfExam(examDto.getNameOfExam());
        exam.setDateOfExam(examDto.getDateOfExam());
        return exam;
    }


    public static ExamDto toDto(Exam exam) {
        ExamDto examDto = new ExamDto();
        examDto.setNameOfExam(exam.getNameOfExam());
        examDto.setDateOfExam(exam.getDateOfExam());
        if (exam.getSubject() != null) {
            examDto.setSubjectId(exam.getSubject().getId());
        }
        examDto.setDateOfExam(exam.getDateOfExam());
        if (exam.getStudent() != null) {
            examDto.setStudentId(exam.getStudent().getId());
        }
        return examDto;
    }
}

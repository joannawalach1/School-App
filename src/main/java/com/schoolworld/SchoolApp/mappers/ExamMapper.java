package com.schoolworld.SchoolApp.mappers;

import com.schoolworld.SchoolApp.domain.Exam;
import com.schoolworld.SchoolApp.domain.Student;
import com.schoolworld.SchoolApp.domain.Subject;
import com.schoolworld.SchoolApp.domain.dto.ExamDto;
import com.schoolworld.SchoolApp.repository.StudentRepo;
import com.schoolworld.SchoolApp.repository.SubjectRepo;
import com.schoolworld.SchoolApp.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExamMapper {
    private final SubjectRepo subjectRepo;
    private final StudentRepo studentRepo;

    public Exam toEntity(ExamDto examDto) {
        Exam exam = new Exam();
        exam.setNameOfExam(examDto.getNameOfExam());
        exam.setDateOfExam(examDto.getDateOfExam());

        Student student = studentRepo.findById(examDto.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + examDto.getStudentId()));
        exam.setStudent(student);

        Subject subject = subjectRepo.findById(examDto.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found with ID: " + examDto.getSubjectId()));
        exam.setSubject(subject);
        return exam;
    }

    public ExamDto toDto(Exam exam) {
        ExamDto examDto = new ExamDto();
        examDto.setNameOfExam(exam.getNameOfExam());
        examDto.setDateOfExam(exam.getDateOfExam());
        if (exam.getStudent() != null) {
            examDto.setStudentId(exam.getStudent().getId());
        }

        if (exam.getSubject() != null) {
            examDto.setSubjectId(exam.getSubject().getId());
        }
        return examDto;
    }
}

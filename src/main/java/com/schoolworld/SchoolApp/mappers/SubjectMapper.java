package com.schoolworld.SchoolApp.mappers;

import com.schoolworld.SchoolApp.domain.Subject;
import com.schoolworld.SchoolApp.domain.dto.SubjectDto;
import org.springframework.stereotype.Component;

@Component
public class SubjectMapper {
    public Subject toEntity(SubjectDto subjectDto) {
        Subject subject = new Subject();
        subject.setName(subjectDto.getName());
        return subject;
    }

    public SubjectDto toDto(Subject subject) {
        SubjectDto subjectDto = new SubjectDto();
        subjectDto.setName(subject.getName());
        return subjectDto;
    }

}

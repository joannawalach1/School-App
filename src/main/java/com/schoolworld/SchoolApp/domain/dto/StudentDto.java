package com.schoolworld.SchoolApp.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {
    private String name;
    private String email;
    public List<ExamDto> exams;

    public StudentDto(String name, String email) {
    }
}

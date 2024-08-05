package com.schoolworld.SchoolApp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.schoolworld.SchoolApp.config.IntegrationTestConfig;
import com.schoolworld.SchoolApp.domain.Student;
import com.schoolworld.SchoolApp.domain.dto.StudentDto;
import com.schoolworld.SchoolApp.repository.StudentRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTest extends IntegrationTestConfig {

    @Autowired
    private StudentRepo studentRepo;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    private Student student1;
    private Student student2;

    @BeforeEach
    void setUp() {
        student1 = new Student("Jan", "jan@op.pl");
        student2 = new Student("Anna", "anna@op.pl");

        studentRepo.save(student1);
        studentRepo.save(student2);
    }

    @AfterEach
    void tearDown() {
        studentRepo.deleteAll();
    }

    @Test
    void shouldFindStudentByEmail() throws Exception {
        String studentEmail = "jan@op.pl";
        mockMvc.perform(MockMvcRequestBuilders.get("/student/findByEmail/{email}", studentEmail))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Jan"))
                .andExpect(jsonPath("$.email").value("jan@op.pl"))
                .andReturn();
    }

    @Test
    void shouldCreateStudent() throws Exception {
        StudentDto newStudent = new StudentDto();
        newStudent.setName("Ewa");
        newStudent.setEmail("ewa@op.pl");
        newStudent.setExams(new ArrayList<>());

        String studentJson = objectMapper.writeValueAsString(newStudent);

        mockMvc.perform(MockMvcRequestBuilders.post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Ewa"))
                .andExpect(jsonPath("$.email").value("ewa@op.pl"))
                .andExpect(jsonPath("$.exams").isArray())
                .andReturn();
    }


    @Test
    void shouldDeleteStudent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/student/delete/{id}", student2.getId()))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    void shouldUpdateStudent() throws Exception {
        Student student = new Student("Kasia", "kasia@op.pl");
        studentRepo.save(student);

        Student updatedStudent = new Student("Katarzyna", "katarzyna@op.pl");
        String updatedStudentJson = objectMapper.writeValueAsString(updatedStudent);

        mockMvc.perform(MockMvcRequestBuilders.put("/student/update/{studentId}", student1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedStudentJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Katarzyna"))
                .andExpect(jsonPath("$.email").value("katarzyna@op.pl"))
                .andReturn();
    }
}

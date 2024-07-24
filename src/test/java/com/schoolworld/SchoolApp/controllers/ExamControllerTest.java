package com.schoolworld.SchoolApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.schoolworld.SchoolApp.config.IntegrationTestConfig;
import com.schoolworld.SchoolApp.domain.Exam;
import com.schoolworld.SchoolApp.domain.Student;
import com.schoolworld.SchoolApp.domain.Subject;
import com.schoolworld.SchoolApp.repository.ExamRepo;
import com.schoolworld.SchoolApp.repository.StudentRepo;
import com.schoolworld.SchoolApp.repository.SubjectRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@AutoConfigureMockMvc
class ExamControllerTest extends IntegrationTestConfig {

    @Autowired
    private ExamRepo examRepo;
    @Autowired
    private StudentRepo studentRepo;
    @Autowired
    private SubjectRepo subjectRepo;
    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Student student;
    private Subject subject;
    private Exam exam;

    @BeforeEach
    void setUp1() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        student = new Student("Jan", "jan@op.pl");
        subject = new Subject("WOS");
        exam = new Exam("Egamin z matematyki", LocalDateTime.now(), student, subject);
        studentRepo.save(student);
        subjectRepo.save(subject);
        examRepo.save(exam);
    }

    @AfterEach
    void tearDown() {
        studentRepo.deleteAll();
        subjectRepo.deleteAll();
        examRepo.deleteAll();
    }

    @Test
    void getById() throws Exception {
        Exam newExam = new Exam("Egzamin z biologii", LocalDateTime.now(), student, subject);
        examRepo.save(newExam);
        mockMvc.perform(MockMvcRequestBuilders.get("/exam/find/{id}", newExam.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nameOfExam").value("Egzamin z biologii"))
                .andReturn();
    }
//TODO not working
    @Test
    void create() throws Exception {
        Exam newExam = new Exam("Egzamin maturalny 1", LocalDateTime.now(), student, subject);
        String examJson = objectMapper.writeValueAsString(newExam);

        mockMvc.perform(MockMvcRequestBuilders.post("/exam")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(examJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nameOfExam").value("Egzamin maturalny 1"))
                .andReturn();
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/exam/delete/{id}", exam.getId()))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    void update() throws Exception {
        Exam updatedExam = new Exam("Egzamin z chemii", LocalDateTime.now(), student, subject);
        examRepo.save(updatedExam);
        String updatedExamJson = objectMapper.writeValueAsString(updatedExam);

        mockMvc.perform(MockMvcRequestBuilders.put("/exam/put/{id}", updatedExam.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedExamJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nameOfExam").value("Egzamin z chemii"))
                .andReturn();
    }
}

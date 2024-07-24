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
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SubjectControllerTest extends IntegrationTestConfig {
 @Autowired
 private SubjectRepo subjectRepo;
 @Autowired
    private ExamRepo examRepo;
 @Autowired
 private MockMvc mockMvc;
 private ObjectMapper objectMapper;
 @Autowired
 private StudentRepo studentRepo;
    private Subject subject1;
    private Subject subject2;
    private Student student;
    private Exam exam;

//TODO not working
    @BeforeEach
    void setUp1() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        subject1 = new Subject("chemia");
        subject2 = new Subject("fizyka");

        subjectRepo.save(subject1);
        subjectRepo.save(subject2);

        student = new Student("Zofia", "zosia@op.pl");
        studentRepo.save(student);

        exam = new Exam("W44", LocalDateTime.now(), student, subject1);
        subject1.setExams(List.of(exam));
        subjectRepo.save(subject1);
    }
    @AfterEach
    void tearDown() {
        examRepo.deleteAll();
        subjectRepo.deleteAll();
    }
    @Test
    void saveSubject() throws Exception {
        Subject newSubject = new Subject("WRT");
        String subjectJson = objectMapper.writeValueAsString(newSubject);
        mockMvc.perform(MockMvcRequestBuilders.post("/subject")
                .contentType(MediaType.APPLICATION_JSON)
                .content(subjectJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("WRT"))
                .andReturn();
    }

    @Test
    void findById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/subject/find/{id}", subject1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("fizyka"))
                .andReturn();
    }

    @Test
    void updateSubject() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/subject/put/{id}", subject1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("art"))
                .andReturn();
    }

    @Test
    void deleteById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/subject/delete/{id}", subject1.getId()))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    void findAllSubjects() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/subject/getSubjects/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andReturn();
    }
}
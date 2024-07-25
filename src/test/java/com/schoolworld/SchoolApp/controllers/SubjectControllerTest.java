package com.schoolworld.SchoolApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.schoolworld.SchoolApp.config.IntegrationTestConfig;
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

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SubjectControllerTest extends IntegrationTestConfig {

    @Autowired
    private SubjectRepo subjectRepo;
    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Subject subject1;
    @Autowired
    private ExamRepo examRepo;
    @Autowired
    private StudentRepo studentRepo;

    @BeforeEach
    void setUp1() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        subject1 = new Subject("Maths");

        subjectRepo.save(subject1);
    }

    @AfterEach
    void tearDown() {
        studentRepo.deleteAll();
        examRepo.deleteAll();
        subjectRepo.deleteAll();
    }

    @Test
    void getById() throws Exception {
        Subject subject = new Subject("M2");
        subjectRepo.save(subject);

        mockMvc.perform(MockMvcRequestBuilders.get("/subject/find/{id}", subject.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("M2"))
                .andReturn();
    }

    @Test
    void create() throws Exception {
        Subject newSubject = new Subject("MM");
        String studentJson = objectMapper.writeValueAsString(newSubject);

        mockMvc.perform(MockMvcRequestBuilders.post("/subject")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void delete() throws Exception {
        Subject subject = new Subject("M2");
        subjectRepo.save(subject);

        mockMvc.perform(MockMvcRequestBuilders.delete("/subject/delete/{id}", subject.getId()))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    void update() throws Exception {
        Subject subject = new Subject("M3");
subjectRepo.save(subject);
        Subject updatedSubject = new Subject("M4");
        subjectRepo.save(updatedSubject);

        String updatedSubjectJson = objectMapper.writeValueAsString(updatedSubject);

        mockMvc.perform(MockMvcRequestBuilders.put("/subject/update/{id}", updatedSubject.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedSubjectJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("M4"))
                .andReturn();
    }
}

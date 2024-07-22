package com.schoolworld.SchoolApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.schoolworld.SchoolApp.domain.Student;
import com.schoolworld.SchoolApp.domain.dto.StudentDto;
import com.schoolworld.SchoolApp.repository.StudentRepo;
import com.schoolworld.SchoolApp.service.ExamService;
import com.schoolworld.SchoolApp.service.StudentService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class StudentControllerTest {

    @Autowired
    private MockMvc mockMVc;

    @MockBean
    private StudentService studentService;

    private ObjectMapper objectMapper;
    private Student student;
    @Autowired
    private StudentRepo studentRepo;

    @BeforeAll
    public static void setUp() {
        PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13")
                .withDatabaseName("school1")
                .withUsername("postgres")
                .withPassword("666666");

        postgres.start();
        System.setProperty("spring.datasource.url", postgres.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgres.getUsername());
        System.setProperty("spring.datasource.password", postgres.getPassword());
    }

    @BeforeEach
    void setUp1() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
            Student student1 = new Student("Jan", "jan@op.pl");
            Student student2 = new Student("Anna", "anna@op.pl");

            studentRepo.save(student1);
            studentRepo.save(student2);
        }

    @Test
    void getByEmail() throws Exception {
        String studentEmail = "kowalski@op.pl";
        StudentDto studentDto = new StudentDto("kowalski", "kowalski@op.pl");
        when(studentService.findByEmail("kowalski@op.pl")).thenReturn(Optional.of(studentDto));
        MvcResult result = mockMVc.perform(MockMvcRequestBuilders.get("/student/findByEmail/{email}", studentEmail))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        String json = result.getResponse().getContentAsString();
        StudentDto studentFromRequest = objectMapper.readValue(json, StudentDto.class);
        assertEquals(studentDto, studentFromRequest);
    }

    @Test
    void create() throws Exception {
        StudentDto studentDto = new StudentDto("kowalski", "kowalski@op.pl");
        when(studentService.save(any())).thenReturn((studentDto));
        String json = objectMapper.writeValueAsString(new StudentDto());
        MvcResult result = mockMVc.perform(MockMvcRequestBuilders.post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        json = result.getResponse().getContentAsString();
        StudentDto studentFromRequest = objectMapper.readValue(json, StudentDto.class);
        assertEquals(studentDto, studentFromRequest);
    }

    @Test
    void delete() throws Exception {
        Long studentId = 1L;
        doNothing().when(studentService).deleteStudent(studentId);
        mockMVc.perform(MockMvcRequestBuilders.delete("/student/delete/{id}", studentId))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    void update() throws Exception {
        Long studentId = 1L;
        StudentDto studentDto = new StudentDto("kowalski", "kowalski@op.pl");
        when(studentService.updateStudent(eq(studentId), any(StudentDto.class))).thenReturn(studentDto);
        String json = objectMapper.writeValueAsString(studentDto);
        MvcResult result = mockMVc.perform(MockMvcRequestBuilders.put("/student/update/{studentId}", studentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        json = result.getResponse().getContentAsString();
        StudentDto studentFromRequest = objectMapper.readValue(json, StudentDto.class);
        assertEquals(studentDto, studentFromRequest);
    }
}






package com.schoolworld.SchoolApp.repository;

import com.schoolworld.SchoolApp.domain.Exam;
import com.schoolworld.SchoolApp.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepo extends JpaRepository<Exam, Long> {
    boolean findByNameOfExam(String nameOfExam);

    List<Exam> findByStudentId(Long id);

    List<Exam> findByStudent(Student student);
}

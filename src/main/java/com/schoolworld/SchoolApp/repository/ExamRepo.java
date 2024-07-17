package com.schoolworld.SchoolApp.repository;

import com.schoolworld.SchoolApp.domain.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRepo extends JpaRepository<Exam, Long> {
    boolean findByNameOfExam(String nameOfExam);
}

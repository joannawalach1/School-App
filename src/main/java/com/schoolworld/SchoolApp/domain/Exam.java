package com.schoolworld.SchoolApp.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name="exam")
public class Exam{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nameOfExam;
    private LocalDateTime dateOfExam;
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = true)
    private Student student;
    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = true)
    private Subject subject;
    @CreationTimestamp
    private LocalDateTime created;
    @UpdateTimestamp
    private LocalDateTime updated;

    public Exam(String nameOfExam, LocalDateTime dateOfExam, Student student, Subject subject) {
        this.nameOfExam = nameOfExam;
        this.dateOfExam = dateOfExam;
        this.student = student;
        this.subject = subject;
    }
}

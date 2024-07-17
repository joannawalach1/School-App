package com.schoolworld.SchoolApp.exceptions;

public class StudentWithSuchIdExists extends Exception {
    public StudentWithSuchIdExists(String message) {
        super(message);
    }
}

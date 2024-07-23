package com.schoolworld.SchoolApp.exceptions;

public class StudentWithSuchEmailExists extends Throwable {
    public StudentWithSuchEmailExists(String message) {
        super(message);
    }
}

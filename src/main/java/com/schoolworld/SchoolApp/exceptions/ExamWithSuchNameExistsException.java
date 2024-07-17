package com.schoolworld.SchoolApp.exceptions;

public class ExamWithSuchNameExistsException extends Exception {
    public ExamWithSuchNameExistsException(String message) {
        super(message);
    }
}

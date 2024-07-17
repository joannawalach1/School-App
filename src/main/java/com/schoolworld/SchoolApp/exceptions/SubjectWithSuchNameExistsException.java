package com.schoolworld.SchoolApp.exceptions;

public class SubjectWithSuchNameExistsException extends Exception {
    public SubjectWithSuchNameExistsException(String message) {
        super(message);
    }
}

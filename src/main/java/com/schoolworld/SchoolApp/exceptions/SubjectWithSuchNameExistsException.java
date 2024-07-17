package com.schoolworld.SchoolApp.exceptions;

public class SubjectWithSuchNameExistsException extends Throwable {
    public SubjectWithSuchNameExistsException(String message) {
        super(message);
    }
}

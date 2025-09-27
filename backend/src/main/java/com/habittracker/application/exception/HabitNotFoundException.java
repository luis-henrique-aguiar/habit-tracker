package com.habittracker.application.exception;

public class HabitNotFoundException extends ApplicationException {

    public HabitNotFoundException(String message) {
        super(message);
    }

    public HabitNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

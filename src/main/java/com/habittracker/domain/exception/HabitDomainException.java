package com.habittracker.domain.exception;

public class HabitDomainException extends RuntimeException {

    public HabitDomainException(String message) {
        super(message);
    }

    public HabitDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}

package com.habittracker.application.exception;

import java.util.List;

public class ValidationException extends ApplicationException {

    private final List<String> validationErrors;

    public ValidationException(String message, List<String> validationErrors) {
        super(message);
        this.validationErrors = validationErrors != null ? List.copyOf(validationErrors) : List.of();
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }
}

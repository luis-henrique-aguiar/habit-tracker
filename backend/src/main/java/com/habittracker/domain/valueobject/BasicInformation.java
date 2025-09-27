package com.habittracker.domain.valueobject;

import com.habittracker.domain.exception.HabitDomainException;

public record BasicInformation(String name, String description) {

    public BasicInformation {
        validateName(name);
        validateDescription(description);
    }

    public static BasicInformation create(String name, String description) {
        String normalizedName = name != null ? name.trim() : null;
        String normalizedDescription = description != null ? description.trim() : null;

        return new BasicInformation(normalizedName, normalizedDescription);
    }

    public BasicInformation update(String newName, String newDescription) {
        return create(newName, newDescription);
    }

    public boolean hasDescription() {
        return description != null && !description.isEmpty();
    }

    private static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new HabitDomainException("Nome do hábito é obrigatório.");
        }
        if (name.trim().length() > 100) {
            throw new HabitDomainException("Nome do hábito não pode ter mais de 100 caracteres");
        }
    }

    private static void validateDescription(String description) {
        if (description != null && description.trim().length() > 500) {
            throw new HabitDomainException("Descrição não pode ter mais de 500 caracteres.");
        }
    }
}

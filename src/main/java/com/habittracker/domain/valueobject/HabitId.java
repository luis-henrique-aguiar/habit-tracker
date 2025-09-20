package com.habittracker.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

public record HabitId(UUID value) {

    public HabitId {
        Objects.requireNonNull(value, "Habit ID value cannot be null.");
    }

    public static HabitId generate() {
        return new HabitId(UUID.randomUUID());
    }

    public static HabitId from(String value) {
        try {
            return new HabitId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid format for Habit ID: " + value);
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

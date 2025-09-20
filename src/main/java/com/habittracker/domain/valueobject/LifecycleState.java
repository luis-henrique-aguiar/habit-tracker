package com.habittracker.domain.valueobject;

import java.time.LocalDateTime;
import java.time.Period;

public record LifecycleState(boolean active, LocalDateTime createdAt) {

    public LifecycleState {
        if (createdAt == null) {
            throw new IllegalArgumentException("Data da criação não pode ser nula.");
        }
        if (createdAt.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Data de criação não pode estar no futuro.");
        }
    }

    public static LifecycleState newHabit() {
        return new LifecycleState(true, LocalDateTime.now());
    }

    public LifecycleState deactivate() {
        return new LifecycleState(false, this.createdAt);
    }

    public LifecycleState activate() {
        return new LifecycleState(true, this.createdAt);
    }

    public boolean isVeteran() {
        return Period.between(createdAt.toLocalDate(), LocalDateTime.now().toLocalDate()).getDays() >= 30;
    }

    public boolean isRecent() {
        return Period.between(createdAt.toLocalDate(), LocalDateTime.now().toLocalDate()).getDays() >= 7;
    }

    public int daysOfExistence() {
        return Period.between(createdAt.toLocalDate(), LocalDateTime.now().toLocalDate()).getDays();
    }

    public boolean canBeModified() {
        return active;
    }
}

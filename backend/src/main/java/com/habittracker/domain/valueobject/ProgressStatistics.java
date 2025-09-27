package com.habittracker.domain.valueobject;

import com.habittracker.domain.exception.HabitDomainException;

public record ProgressStatistics(int currentStreak, int bestStreak) {

    public ProgressStatistics {
        if (currentStreak < 0) {
            throw new HabitDomainException("Sequência atual não pode ser negativa.");
        }
        if (bestStreak < 0) {
            throw new HabitDomainException("Melhor sequência nãao pode ser negativa.");
        }
        if (bestStreak < currentStreak) {
            throw new HabitDomainException("Melhor sequência deve ser maior ou igual a sequência atual.");
        }
    }

    public static ProgressStatistics initial() {
        return new ProgressStatistics(0, 0);
    }

    public ProgressStatistics incrementStreak() {
        int newCurrentStreak = this.currentStreak + 1;
        int newBestStreak = Math.max(this.bestStreak, newCurrentStreak);

        return new ProgressStatistics(newCurrentStreak, newBestStreak);
    }

    public ProgressStatistics breakStreak() {
        return new ProgressStatistics(0, this.bestStreak);
    }

    public boolean isInGoodStreak() {
        return this.currentStreak >= 7;
    }

    public boolean isInExceptionalStreak() {
        return this.currentStreak >= 30;
    }
}

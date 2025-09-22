package com.habittracker.domain.entity;

import com.habittracker.domain.exception.HabitDomainException;
import com.habittracker.domain.valueobject.*;
import lombok.Getter;

import java.util.Objects;

@Getter
public class Habit {

    private final HabitId id;
    private final UserId userId;
    private BasicInformation information;
    private ProgressStatistics statistics;
    private LifecycleState state;

    public Habit(HabitId id, UserId userId, String name, String description) {
        this.id = Objects.requireNonNull(id, "Habit ID cannot be null.");
        this.userId = Objects.requireNonNull(userId, "ID do usuário não pode ser nulo.");
        this.information = BasicInformation.create(name, description);
        this.statistics = ProgressStatistics.initial();
        this.state = LifecycleState.newHabit();
    }

    public Habit(HabitId id, UserId userId, BasicInformation information, ProgressStatistics statistics, LifecycleState state) {
        this.id = Objects.requireNonNull(id, "ID do hábito não pode ser nulo");
        this.userId = Objects.requireNonNull(userId, "ID do usuário não pode ser nulo");
        this.information = Objects.requireNonNull(information, "Informações básicas não podem ser nulas");
        this.statistics = Objects.requireNonNull(statistics, "Estatísticas não podem ser nulas");
        this.state = Objects.requireNonNull(state, "Estado não pode ser nulo");
    }

    public void update(String newName, String newDescription) {
        validateHabitActive();
        this.information = this.information.update(newName, newDescription);
    }

    public void incrementStreak() {
        validateHabitActive();
        this.statistics = this.statistics.incrementStreak();
    }

    public void breakStreak() {
        validateHabitActive();
        this.statistics = this.statistics.breakStreak();
    }

    public void deactivate() {
        if (!this.state.active()) {
            throw new HabitDomainException("Hábito já está desativado.");
        }
        this.state = this.state.deactivate();
    }

    public void reactivate() {
        if (this.state.active()) {
            throw new HabitDomainException("Hábito já está ativo.");
        }
        this.state = this.state.activate();
    }

    public boolean isInGoodStreak() {
        return this.statistics.isInGoodStreak();
    }

    public boolean isVeteran() {
        return this.state.isVeteran();
    }

    public String getName() {
        return this.information.name();
    }

    public String getDescription() {
        return this.information.description();
    }

    public boolean isActive() {
        return this.state.active();
    }

    public int getCurrentStreak() {
        return this.statistics.currentStreak();
    }

    public int getBestStreak() {
        return this.statistics.bestStreak();
    }

    private void validateHabitActive() {
        if (!this.state.canBeModified()) {
            throw new HabitDomainException("Não é possível modificar um hábito desativado.");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Habit habit = (Habit) obj;
        return Objects.equals(id, habit.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Habit{id=%s, name='%s', active=%s, currentStreak=%d}",
                id, getName(), isActive(), getCurrentStreak());
    }
}

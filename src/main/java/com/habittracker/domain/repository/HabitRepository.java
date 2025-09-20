package com.habittracker.domain.repository;

import com.habittracker.domain.entity.Habit;
import com.habittracker.domain.valueobject.HabitId;
import com.habittracker.domain.valueobject.UserId;

import java.util.List;
import java.util.Optional;

public interface HabitRepository {

    Habit save(Habit habit);

    Optional<Habit> findById(HabitId habitId);

    List<Habit> findActiveByUserId(UserId userId);

    List<Habit> findAllByUserId(UserId userId);

    boolean existsByUserIdAndName(UserId userId, String name);

    void deleteById(HabitId habitId);

    long countActiveByUserId(UserId userId);

}

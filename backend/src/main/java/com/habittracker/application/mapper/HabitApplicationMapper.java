package com.habittracker.application.mapper;

import com.habittracker.application.dto.response.HabitResponse;
import com.habittracker.application.dto.response.UserHabitsStatisticsResponse;
import com.habittracker.domain.entity.Habit;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HabitApplicationMapper {

    public HabitResponse toResponse(Habit habit) {
        if (habit == null) return null;

        return new HabitResponse(
                habit.getId().toString(),
                habit.getUserId().toString(),
                habit.getName(),
                habit.getDescription(),
                habit.isActive(),
                habit.getCurrentStreak(),
                habit.getBestStreak(),
                habit.getState().createdAt(),
                habit.isInGoodStreak(),
                habit.isVeteran()
        );
    }

    public UserHabitsStatisticsResponse toStatisticsResponse(String userId, List<Habit> habits) {
        if (habits == null || habits.isEmpty()) {
            return new UserHabitsStatisticsResponse(userId, 0, 0, 0, 0, 0.0);
        }

        int totalHabits = habits.size();

        int activeHabits = (int) habits.stream()
                .filter(Habit::isActive)
                .count();

        int habitsInGoodStreak = (int) habits.stream()
                .filter(Habit::isInGoodStreak)
                .count();

        int veteranHabits = (int) habits.stream()
                .filter(Habit::isVeteran)
                .count();

        double averageStreak = habits.stream()
                .mapToInt(Habit::getCurrentStreak)
                .average()
                .orElse(0.0);

        return new UserHabitsStatisticsResponse(
                userId,
                totalHabits,
                activeHabits,
                habitsInGoodStreak,
                veteranHabits,
                Math.round(averageStreak * 100.0) / 100.0
        );
    }
}

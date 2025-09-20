package com.habittracker.application.dto.response;

public record UserHabitsStatisticsResponse(
    String userId,
    int totalHabits,
    int activeHabits,
    int habitsInGoodStreak,
    int veteranHabits,
    double averageStreak
) { }

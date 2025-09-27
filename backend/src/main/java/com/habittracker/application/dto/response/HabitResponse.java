package com.habittracker.application.dto.response;

import java.time.LocalDateTime;

public record HabitResponse(
    String id,
    String userId,
    String name,
    String description,
    boolean active,
    int currentStreak,
    int bestStreak,
    LocalDateTime createdAt,
    boolean isInGoodStreak,
    boolean isVeteran
) {}

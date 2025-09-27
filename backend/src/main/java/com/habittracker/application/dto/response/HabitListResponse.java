package com.habittracker.application.dto.response;

import java.util.List;

public record HabitListResponse(
    List<HabitResponse> habits,
    int totalCount,
    int activeCount,
    int inactiveCount
) {}

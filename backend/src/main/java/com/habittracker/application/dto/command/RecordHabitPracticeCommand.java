package com.habittracker.application.dto.command;

import jakarta.validation.constraints.NotNull;

public record RecordHabitPracticeCommand(
    @NotNull(message = "ID do hábito é obrigatório")
    String habitId,

    @NotNull(message = "ID do usuário é obrigatório")
    String userId
) {}
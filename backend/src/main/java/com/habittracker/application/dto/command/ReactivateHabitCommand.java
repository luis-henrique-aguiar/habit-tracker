package com.habittracker.application.dto.command;

import jakarta.validation.constraints.NotNull;

public record ReactivateHabitCommand(
    @NotNull(message = "ID do usuário é obriatório.")
    String userId,

    @NotNull(message = "ID do hábito é obrigatório.")
    String habitId
) {}

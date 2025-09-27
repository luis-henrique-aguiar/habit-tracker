package com.habittracker.application.dto.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateHabitCommand(
    @NotNull(message = "ID do usuário é obrigatório.")
    String userId,

    @NotBlank(message = "Nome do hábito é obrigatório.")
    @Size(max = 100, message = "Nome não pode ter mais de 100 caracteres.")
    String name,

    @Size(max = 500, message = "Descrição não pode ter mais de 500 caracteres.")
    String description
) {}

package com.habittracker.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Requisição paraa criação de novo hábito.")
public record CreateHabitRequest(

    @Schema(description = "Nome do hábito", example = "Meditar", maxLength = 100)
    @NotBlank(message = "Nome do hábito é obrigatório")
    @Size(max = 100, message = "Nome não pode ter mais de 100 caracteres.")
    String name,

    @Schema(description = "Descrição opcional do hábito",
            example = "10 minutos de meditação mindfulness", maxLength = 500)
    @Size(max = 500, message = "Descrição não pode ter mais de 500 caracteres")
    String description

) {}

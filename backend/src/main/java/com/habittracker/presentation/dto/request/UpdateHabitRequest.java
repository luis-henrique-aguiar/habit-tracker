package com.habittracker.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Requisição para atualização de hábito existente")
public record UpdateHabitRequest(

    @Schema(description = "Nome do hábito", example = "Meditaar pela manhã", maxLength = 100)
    @NotBlank(message = "Nome do hábito é obrigatório")
    @Size(max = 100, message = "Nome não pode ter mais de 100 caracteres")
    String name,

    @Schema(description = "Descrição do hábito",
            example = "15 minutps de meditação mindfulness logo após acordar", maxLength = 100)
    @Size(max = 500, message = "Descrição não pode ter mais de 500 caracteres")
    String description

) {}

package com.habittracker.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "List de hábitos com informações agregadas")
public record HabitListApiResponse(

        @Schema(description = "Lista de hábitos")
        List<HabitApiResponse> habits,

        @Schema(description = "Metadados de lista")
        ListMetadata metadata

) {

    @Schema(description = "Metadados sobre a listaa de hábitos")
    public record ListMetadata(
            @Schema(description = "Total de hábitos", example = "5")
            int total,

            @Schema(description = "Hábitos ativos", example = "4")
            int active,

            @Schema(description = "Hábitos inativos", example = "1")
            int inactive,

            @Schema(description = "Hábitos em boa sequência", example = "2")
            int inGoodStreak
    ) {}
}

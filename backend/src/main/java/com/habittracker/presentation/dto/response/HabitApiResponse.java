package com.habittracker.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Resposta contento dados de um hábito")
public record HabitApiResponse(

        @Schema(description = "Identificador único do hábito", example = "550e8400-e29b-41d4-a716-446655440000")
        String id,

        @Schema(description = "Nome do hábito", example = "Meditar")
        String name,

        @Schema(description = "Descrição do hábito", example = "10 minutos de meditação mindfulness")
        String description,

        @Schema(description = "Indica se o hábito está ativo", example = "true")
        @JsonProperty("is_active")
        boolean active,

        @Schema(description = "Sequência atual de dias consecutivos", example = "15")
        @JsonProperty("current_streak")
        int currentStreak,

        @Schema(description = "Melhor sequência já alcançada", example = "42")
        @JsonProperty("best_streak")
        int bestStreak,

        @Schema(description = "Data de criação do hábito")
        @JsonProperty("created_at")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt,

        @Schema(description = "Indica se está em uma boa sequência (7+ diaas)", example = "true")
        @JsonProperty("is_in_good_streak")
        boolean inGoodStreak,

        @Schema(description = "Indica se é um hábito veterano (30+ dias)", example = "false")
        @JsonProperty("is_veteran")
        boolean veteran,

        @Schema(description = "Status descritivo da sequência", example = "Em boa sequẽncia!")
        @JsonProperty("streak_status")
        String streakStatus

) {

    private static final String FIRE_ICON = "\uD83D\uDD25";
    private static final String MUSCLE_ICON = "\uD83D\uDCAA";
    private static final String CHART_ICON = "\uD83D\uDCC8";
    private static final String THUMBS_UP_ICON = "\uD83D\uDC4D";

    public static HabitApiResponse from(String id, String name, String description, boolean active, int currentStreak,
                                        int bestStreak, LocalDateTime createdAt, boolean inGoodStreak, boolean veteran) {

        String streakStatus = calculateStreakStatus(currentStreak, inGoodStreak);

        return new HabitApiResponse(id, name, description, active, currentStreak, bestStreak, createdAt,
                inGoodStreak, veteran, streakStatus);

    }

    private static String calculateStreakStatus(int currentStreak, boolean inGoodStreak) {
        if (currentStreak == 0) return "Comece a suaa jornada!";
        if (currentStreak >= 30) return "Sequência Excepcional! " + FIRE_ICON;
        if (inGoodStreak) return "Em boa sequência! " + MUSCLE_ICON;
        if (currentStreak >= 3) return "Ganhando momentum! " + CHART_ICON;
        return "Primeiros passos " + THUMBS_UP_ICON;
    }
}

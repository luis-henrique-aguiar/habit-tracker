package com.habittracker.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Resposta de sucesso padronizada")
public record SuccessApiResponse(

        @Schema(description = "Status de operação", example = "success")
        String status,

        @Schema(description = "Mensagem de sucesso", example = "Hábito removido com sucesso")
        String message,

        @Schema(description = "Timestamp da operação")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime timestamp

) {

    public static SuccessApiResponse create(String message) {
        return new SuccessApiResponse("success", message, LocalDateTime.now());
    }
}

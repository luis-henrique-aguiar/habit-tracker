package com.habittracker.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Resposta de erro padronizada")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorApiResponse(

        @Schema(description = "Código de erro HTTP", example = "400")
        int status,

        @Schema(description = "Tipo de erro", example = "VAALIDATION_ERROR")
        String error,

        @Schema(description = "Mensagem principal do erro", example = "Dados de entrada inválidos")
        String message,

        @Schema(description = "Lista detalhada de erros de validação")
        List<String> details,

        @Schema(description = "Caminho da requisição que causou o erro", example = "/api/v1/habits")
        String path,

        @Schema(description = "Timestamp do erro")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime timestamp,

        @Schema(description = "ID único para rastreamento do erro", example = "550e8400-e29b-41d4-a716-446655440000")
        String traceId

) {

    public static ErrorApiResponse validation(String message, List<String> details, String path, String traceId) {
        return new ErrorApiResponse(400, "VALIDATION_ERROR", message, details, path,
                LocalDateTime.now(), traceId);
    }

    public static ErrorApiResponse notFound(String message, String path, String traceId) {
        return new ErrorApiResponse(404, "NOT_FOUND", message, null, path,
                LocalDateTime.now(), traceId);
    }

    public static ErrorApiResponse internal(String message, String path, String traceId) {
        return new ErrorApiResponse(500, "INTERNAL_ERROR", message, null, path,
                LocalDateTime.now(), traceId);
    }
}

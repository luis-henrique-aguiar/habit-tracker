package com.habittracker.presentation.exception;

import com.habittracker.application.exception.ApplicationException;
import com.habittracker.application.exception.HabitNotFoundException;
import com.habittracker.application.exception.ValidationException;
import com.habittracker.presentation.dto.response.ErrorApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorApiResponse> handleValidationError(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String traceId = generateTraceId();

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        log.warn("Erro de validação na requisição {}: {} [traceId: {}]", request.getRequestURI(), errors, traceId);

        var errorResponse = ErrorApiResponse.validation(
                "Dados de entrada inválidos",
                errors,
                request.getRequestId(),
                traceId
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorApiResponse> handleApplicationValidationErrors(
            ValidationException ex,
            HttpServletRequest request) {

        String traceId = generateTraceId();

        log.warn("Erro de validação da aplicação na requisição {}: {} [traceId: {}]",
                request.getRequestURI(), ex.getMessage(), traceId);

        var errorResponse = ErrorApiResponse.validation(
            ex.getMessage(),
            ex.getValidationErrors(),
            request.getRequestURI(),
            traceId
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(HabitNotFoundException.class)
    public ResponseEntity<ErrorApiResponse> handleHabitNotFound(
            HabitNotFoundException ex,
            HttpServletRequest request) {

        String traceId = generateTraceId();

        log.warn("Hábito não encontrado na requisição {}: {} [traceId: {}]",
                request.getRequestURI(), ex.getMessage(), traceId);

        var errorResponse = ErrorApiResponse.notFound(
                ex.getMessage(),
                request.getRequestURI(),
                traceId
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorApiResponse> handleApplicationError(
            ApplicationException ex,
            HttpServletRequest request) {

        String traceId = generateTraceId();

        log.warn("Erro da aplicação na requisição {}: {} [traceId: {}]",
                request.getRequestURI(), ex.getMessage(), traceId);

        HttpStatus status = determineStatusFromMessage(ex.getMessage());

        var errorResponse = new ErrorApiResponse(
                status.value(),
                "APPLICATION_ERROR",
                ex.getMessage(),
                null,
                request.getRequestURI(),
                LocalDateTime.now(),
                traceId
        );

        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorApiResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {

        String traceId = generateTraceId();

        String message = String.format("Valor inválido '%s' para parâmetro '%s'. Esperado '%s'",
                ex.getValue(),
                ex.getMessage(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "tipo válido"
        );

        log.warn("Error de tipo na requisição {}: {} [traceId: {}]", request.getRequestURI(), message, traceId);

        var errorResponse = ErrorApiResponse.validation(
            "Parâmetro inválido",
            List.of(message),
            request.getRequestURI(),
            traceId
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorApiResponse> handleJsonParseError(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        String traceId = generateTraceId();

        log.warn("Erro de parsing JSON na requisição {}: {} [traceId: {}]",
                request.getRequestURI(), ex.getMessage(), traceId);

        var errorResponse = ErrorApiResponse.validation(
            "Formato JSON inválido",
            List.of("Verifique a estrutura do JSON enviado"),
            request.getRequestURI(),
            traceId
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorApiResponse> handleGenericError(
            Exception ex,
            HttpServletRequest request) {

        String traceId = generateTraceId();

        log.error("Erro interno não tratado na requisição {} [traceId: {}]: ",
                request.getRequestURI(), traceId, ex);

        var errorResponse = ErrorApiResponse.internal(
                "Erro interno do servidor",
                request.getRequestURI(),
                traceId
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    private HttpStatus determineStatusFromMessage(String message) {
        if (message.toLowerCase().contains("não tem permissão") ||
            message.toLowerCase().contains("não autorizado")) {
            return HttpStatus.FORBIDDEN;
        }

        if (message.toLowerCase().contains("limite") || message.toLowerCase().contains("máximo") ||
            message.toLowerCase().contains("já existe") || message.toLowerCase().contains("duplicado")) {
            return HttpStatus.CONFLICT;
        }

        return HttpStatus.BAD_REQUEST;
    }

    private String generateTraceId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}

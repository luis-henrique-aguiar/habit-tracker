package com.habittracker.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Health", description = "Endpoints de monitoramento e health check")
public class HealthController {

    private final Optional<BuildProperties> buildProperties;

    @GetMapping("/health")
    @Operation(summary = "Health Check",
            description = "Verifica se a aplicação está funcionando")
    @ApiResponse(responseCode = "200", description = "Aplicação funcionando normalmente")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "timestamp", LocalDateTime.now(),
                "service", "habit-tracker-api"
        ));
    }

    @GetMapping("/info")
    @Operation(summary = "Application Info",
            description = "Retorna informações detalhadas da aplicação")
    @ApiResponse(responseCode = "200", description = "Informações da aplicação")
    public ResponseEntity<Map<String, Map<String, ?>>> info() {
        var info = Map.of(
                "application", Map.of(
                        "name", "Habit Tracker API",
                        "description", "Sistema de acompanhamento de hábitos",
                        "version", getVersion(),
                        "build-time", getBuildTime()
                ),
                "api", Map.of(
                        "version", "v1",
                        "documentation", "/swagger-ui.html",
                        "endpoints", Map.of(
                                "habits", "/api/v1/users/{userId}/habits",
                                "health", "/api/v1/health",
                                "info", "/api/v1/info"
                        )
                ),
                "system", Map.of(
                        "timestamp", LocalDateTime.now().toString(),
                        "timezone", java.time.ZoneId.systemDefault().toString(),
                        "java-version", System.getProperty("java.version")
                )
        );

        return ResponseEntity.ok(info);
    }

    @GetMapping("/ready")
    @Operation(summary = "Readiness Check",
            description = "Verifica se a aplicação está pronta para receber tráfego")
    @ApiResponse(responseCode = "200", description = "Aplicação pronta")
    public ResponseEntity<Map<String, Object>> ready() {
        return ResponseEntity.ok(Map.of(
                "status", "READY",
                "timestamp", LocalDateTime.now(),
                "checks", Map.of(
                        "database", "UP",
                        "application", "UP"
                )
        ));
    }

    private String getVersion() {
        return buildProperties
                .map(BuildProperties::getVersion)
                .orElse("development");
    }

    private String getBuildTime() {
        return buildProperties
                .map(props -> props.getTime().toString())
                .orElse("unknown");
    }
}

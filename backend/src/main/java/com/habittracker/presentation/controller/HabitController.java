package com.habittracker.presentation.controller;

import com.habittracker.application.dto.command.BreakHabitStreakCommand;
import com.habittracker.application.dto.command.DeactivateHabitCommand;
import com.habittracker.application.dto.command.ReactivateHabitCommand;
import com.habittracker.application.dto.command.RecordHabitPracticeCommand;
import com.habittracker.application.service.HabitApplicationService;
import com.habittracker.presentation.dto.request.CreateHabitRequest;
import com.habittracker.presentation.dto.request.UpdateHabitRequest;
import com.habittracker.presentation.dto.response.HabitApiResponse;
import com.habittracker.presentation.dto.response.HabitListApiResponse;
import com.habittracker.presentation.mapper.HabitPresentationMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/{userId}/habits")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
@Tag(name = "Habits", description = "Operações relacionadas aos hábitos dos usuários")
public class HabitController {

    private final HabitApplicationService habitApplicationService;
    private final HabitPresentationMapper mapper;

    @PostMapping
    @Operation(summary = "Cira um novo hábito", description = "Cria um novo hábito para o usuário especificado")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Hábito criado com sucesso",
                    content = @Content(schema = @Schema(implementation = HabitApiResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "409", description = "Hábito com mesmo nome jáa existente")
    })
    public ResponseEntity<HabitApiResponse> createHabit(
            @Parameter(description = "ID do usuário", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable String userId,

            @Parameter(description = "Dados do hábito a ser criado")
            @Valid @RequestBody CreateHabitRequest request) {

        log.info("Recebida requisição para criar háabito '{}' para usuário: {}", request.name(), userId);

        var command = mapper.toCommand(request, userId);
        var habitResponse = habitApplicationService.createHabit(command);
        var apiResponse = mapper.toApiResponse(habitResponse);

        log.info("Hábito criado com sucesso: {} para usuário: {}", apiResponse.id(), userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping
    @Operation(summary = "Listar hábitos", description = "Retorna todos os hábitos do usuário (ativos e inativos)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de hábitos retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = HabitListApiResponse.class))),
            @ApiResponse(responseCode = "400", description = "ID do usuário inválido")
    })
    public ResponseEntity<HabitListApiResponse> getUserHabits(
            @Parameter(description = "ID do usuário")
            @PathVariable String userId) {

        log.info("Buscando hábitos do usuário: {}", userId);

        var habitListResponse = habitApplicationService.getUserHabits(userId);
        var apiResponse = mapper.toApiResponse(habitListResponse);

        log.info("Retornados {} hábitos para usuário: {}", apiResponse.metadata().total(), userId);

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/active")
    @Operation(summary = "Listar hábitos ativos", description = "Retorna apenmas os hábitos ativos do usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de hábitos ativos retornada com sucesso.",
                    content = @Content(schema = @Schema(implementation = HabitListApiResponse.class))),
            @ApiResponse(responseCode = "400", description = "ID do usuário inválido")
    })
    public ResponseEntity<HabitListApiResponse> getActiveUserHabits(
        @Parameter(description = "ID do usuário")
        @PathVariable String userId) {

        log.info("Buscando hábitos ativos do usuário: {}", userId);

        var habitListResponse = habitApplicationService.getActiveUserHabits(userId);
        var apiResponse = mapper.toApiResponse(habitListResponse);

        log.info("Retornados {} hábtios aativos para o usuário: {}", apiResponse.metadata().total(), userId);

        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/{habitId}")
    @Operation(summary = "Atualizar hábito", description = "Atualiza as informações de um hábito existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Hábito atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Daados de entraad inválidos"),
            @ApiResponse(responseCode = "404", description = "Hábito não encontrado"),
            @ApiResponse(responseCode = "403", description = "Usuário não tem permissão para este hábito")
    })
    public ResponseEntity<HabitApiResponse> updateHabit(
            @Parameter(description = "ID do usuário")
            @PathVariable String userId,

            @Parameter(description = "ID do hábito")
            @PathVariable String habitId,

            @Parameter(description = "Novos dados do usuário")
            @Valid @RequestBody UpdateHabitRequest request) {

        log.info("Atualizando hábito {} para usuário: {}", habitId, userId);

        var command = mapper.toCommand(request, habitId, userId);
        var habitResponse = habitApplicationService.updateHabit(command);
        var apiResponse = mapper.toApiResponse(habitResponse);

        log.info("Hábito {} atualizado com sucesso", habitId);

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/{habitId}/practice")
    @Operation(summary = "Registra prática", description = "Registra que o usuário praticou o hábito, incrementando a sequência")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Prática registrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Hábito não encontrado"),
            @ApiResponse(responseCode = "403", description = "Usuário não tem permissão para este hábito")
    })
    public ResponseEntity<HabitApiResponse> recordHabitPractice(
        @Parameter(description = "ID do usuário")
        @PathVariable String userId,

        @Parameter(description = "ID do hábito")
        @PathVariable String habitId) {

        log.info("Registrando prática do hábito {} para usuário: {}", habitId, userId);

        var command = new RecordHabitPracticeCommand(habitId, userId);
        var habitResponse = habitApplicationService.recordHabitPractice(command);
        var apiResponse = mapper.toApiResponse(habitResponse);

        log.info("Prática registrada. Nova sequência: {}", apiResponse.currentStreak());

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/{habitId}/break-streak")
    @Operation(summary = "Quebrar sequência", description = "Quebra a sequência atual do hábito (reseta para 0)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sequência quebrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Hábito não encontrado"),
            @ApiResponse(responseCode = "403", description = "Usuário não tem permissão para este hábito")
    })
    public ResponseEntity<HabitApiResponse> breakHabitStreak(
        @Parameter(description = "ID do usuário")
        @PathVariable String userId,

        @Parameter(description = "ID do hábito")
        @PathVariable String habitId) {

        log.info("Quebrando sequência do hábito {} para usuário: {}", habitId, userId);

        var command = new BreakHabitStreakCommand(habitId, userId);
        var habitResponse = habitApplicationService.breakHabit(command);
        var apiResponse = mapper.toApiResponse(habitResponse);

        log.info("Sequência quebrada com sucesso para o hábito: {}", habitId);

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/{habitId}/deactivate")
    @Operation(summary = "Desativar hábito",
            description = "Desativa o hábito (mantém histórico, mas remove da lista ativa")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Hábito desativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Hábito não encontrado"),
            @ApiResponse(responseCode = "403", description = "Usuário não tem permissão para este hábito")
    })
    public ResponseEntity<HabitApiResponse> deactivateHabit(
            @Parameter(description = "ID do usuário")
            @PathVariable String userId,

            @Parameter(description = "ID do hábito")
            @PathVariable String habitId) {

        log.info("Desativando hábito {} para usuário: {}", habitId, userId);

        var command = new DeactivateHabitCommand(userId, habitId);
        var habitResponse = habitApplicationService.deactivateHabit(command);
        var apiResponse = mapper.toApiResponse(habitResponse);

        log.info("Hábito {} desativado com sucesso.", habitId);

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/{habitId}/reactivate")
    @Operation(summary = "Reativar hábito", description = "Reativa um hábito previamente desativado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Hábito reativado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Limite de hábitos ativos atingido"),
            @ApiResponse(responseCode = "404", description = "Hábito não encontrado"),
            @ApiResponse(responseCode = "403", description = "Usuário não tem permissão para este hábito")
    })
    public ResponseEntity<HabitApiResponse> reactivateHabit(
        @Parameter(description = "ID do usuário")
        @PathVariable String userId,

        @Parameter(description = "ID do hábito")
        @PathVariable String habitId) {

        log.info("Reactivando hábito {} para usuário: {}", habitId, userId);

        var command = new ReactivateHabitCommand(userId, habitId);
        var habitResponse = habitApplicationService.reactivateHabit(command);
        var apiResponse = mapper.toApiResponse(habitResponse);

        log.info("Hábito {} reativado com sucesso.", habitId);

        return ResponseEntity.ok(apiResponse);
    }
}

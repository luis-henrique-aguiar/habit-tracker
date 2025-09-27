package com.habittracker.application.service;

import com.habittracker.application.dto.command.*;
import com.habittracker.application.dto.response.HabitListResponse;
import com.habittracker.application.dto.response.HabitResponse;
import com.habittracker.application.dto.response.UserHabitsStatisticsResponse;
import com.habittracker.application.exception.ValidationException;
import com.habittracker.application.usecase.*;
import com.habittracker.domain.exception.HabitDomainException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class HabitApplicationService {

    private final CreateHabitUseCase createHabitUseCase;
    private final UpdateHabitUseCase updateHabitUseCase;
    private final RecordHabitPracticeUseCase recordHabitPracticeUseCase;
    private final GetUserHabitsUseCase getUserHabitsUseCase;
    private final DeactivateHabitUseCase deactivateHabitUseCase;
    private final GetActiveUserHabitsUseCase getActiveUserHabitsUseCase;
    private final GetUserHabitsStatisticsUseCase getUserHabitsStatisticsUseCase;
    private final ReactivateHabitUseCase reactivateHabitUseCase;
    private final BreakHabitStreakUseCase breakHabitStreakUseCase;
    private final Validator validator;

    public HabitResponse createHabit(CreateHabitCommand command) {
        log.info("Processando criação de hábito: {}", command.name());

        validateCommand(command);

        try {
            return createHabitUseCase.execute(command);
        } catch (HabitDomainException e) {
            log.warn("Erro de domínio ao criar hábito: {}", e.getMessage());
            throw new ValidationException("Erro de validação: " + e.getMessage(), null);
        }
    }

    public HabitResponse updateHabit(UpdateHabitCommand command) {
        log.info("Processando atualização de hábito: {}", command.habitId());

        validateCommand(command);

        try {
            return updateHabitUseCase.execute(command);
        } catch (HabitDomainException e) {
            log.warn("Erro de domínio ao atualizar hábito: {}", e.getMessage());
            throw new ValidationException("Erro de validação: " + e.getMessage(), null);
        }
    }

    public HabitResponse recordHabitPractice(RecordHabitPracticeCommand command) {
        log.info("Processando registro de prática: {}", command.habitId());

        validateCommand(command);

        try {
            return recordHabitPracticeUseCase.execute(command);
        } catch (HabitDomainException e) {
            log.warn("Erro de domínio ao registrar prática: {}", e.getMessage());
            throw new ValidationException("Erro de validação: " + e.getMessage(), null);
        }
    }

    public HabitResponse deactivateHabit(DeactivateHabitCommand command) {
        log.info("Processando desativação de hábito: {}", command.habitId());

        validateCommand(command);

        try {
            return deactivateHabitUseCase.execute(command);
        } catch (HabitDomainException e) {
            log.warn("Erro de domínio ao desativar hábito: {}", e.getMessage());
            throw new ValidationException("Erro de validação: " + e.getMessage(), null);
        }
    }

    public HabitResponse reactivateHabit(ReactivateHabitCommand command) {
        log.info("Processando reativação de hábito: {}", command.habitId());

        validateCommand(command);

        try {
            return reactivateHabitUseCase.execute(command);
        } catch (HabitDomainException e) {
            log.warn("Erro de domínio ao reativar hábito: {}", e.getMessage());
            throw new ValidationException("Erro de validação: " + e.getMessage(), null);
        }
    }

    public HabitResponse breakHabit(BreakHabitStreakCommand command) {
        log.info("Processando interrupção de sequência do hábito: {}", command.habitId());

        validateCommand(command);

        try {
            return breakHabitStreakUseCase.execute(command);
        } catch (HabitDomainException e) {
            log.warn("Erro de domínio ao quebrar a sequência do hábito: {}", e.getMessage());
            throw new ValidationException("Erro de validação: " + e.getMessage(), null);
        }
    }

    public HabitListResponse getActiveUserHabits(String userIdStr) {
        log.info("Processando busca de lista de hábtios ativos para o usuário: {}", userIdStr);

        validateUserId(userIdStr);

        try {
            return getActiveUserHabitsUseCase.execute(userIdStr);
        } catch (HabitDomainException e) {
            log.warn("Erro de domínio ao recuperar os hábitos ativos do usuário: {}", e.getMessage());
            throw new ValidationException("Erro de validação: " + e.getMessage(), null);
        }
    }

    public HabitListResponse getUserHabits(String userIdStr) {
        log.info("Processando busca de hábitos para o usuário: {}", userIdStr);

        validateUserId(userIdStr);

        try {
            return getUserHabitsUseCase.execute(userIdStr);
        } catch (HabitDomainException e) {
            log.warn("Erro de domínio ao recuperar hábitos do usuário: {}", e.getMessage());
            throw new ValidationException("Erro de validação: " + e.getMessage(), null);
        }
    }

    public UserHabitsStatisticsResponse getUserHabitsStatistics(String userIdStr) {
        log.info("Processando busca de estatísticas do usuário: {}", userIdStr);

        validateUserId(userIdStr);

        return getUserHabitsStatisticsUseCase.execute(userIdStr);
    }

    private <T> void validateCommand(T command) {
        Set<ConstraintViolation<T>> violations = validator.validate(command);

        if (!violations.isEmpty()) {
            var errors = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .toList();

            throw new ValidationException("Dados de entrada inválidos", errors);
        }
    }

    private void validateUserId(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new ValidationException("ID do usuário é obrigatório", null);
        }
    }

    private void validateHabitId(String habitIdStr) {
        if (habitIdStr == null || habitIdStr.trim().isEmpty()) {
            throw new ValidationException("ID do hábito é obrigatório.", null);
        }
    }
}

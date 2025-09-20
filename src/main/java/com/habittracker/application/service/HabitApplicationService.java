package com.habittracker.application.service;

import com.habittracker.application.dto.command.*;
import com.habittracker.application.dto.response.HabitResponse;
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
            throw new ValidationException("Erro de validação: " + e.getMessage(), null);
        }
    }

    public HabitResponse reactivateHabit(ReactivateHabitCommand command) {
        log.info("Processando reativação de hábito: {}", command.habitId());

        validateCommand(command);

        try {
            return reactivateHabitUseCase.execute(command);
        } catch (HabitDomainException e) {
            throw new ValidationException("Erro de validação: " + e.getMessage(), null);
        }
    }

    public HabitResponse breakHabit(BreakHabitStreakCommand command) {
        log.info("Processando interrupção de sequência do hábito: {}", command.habitId());

        validateCommand(command);

        try {
            return breakHabitStreakUseCase.execute(command);
        } catch (HabitDomainException e) {
            throw new ValidationException("Erro de validação: " + e.getMessage(), null);
        }
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
}

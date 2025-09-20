package com.habittracker.application.service;

import com.habittracker.application.dto.command.*;
import com.habittracker.application.dto.response.HabitResponse;
import com.habittracker.application.mapper.HabitApplicationMapper;
import com.habittracker.application.usecase.*;
import com.habittracker.domain.exception.HabitDomainException;
import com.habittracker.domain.repository.HabitRepository;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HabitApplicationService {

    private final CreateHabitUseCase createHabitUseCase;
    private final UpdateHabitUseCase updateHabitUseCase;
    private final RecordHabitPracticeUseCase recordHabitPracticeUseCase;
    private final GetUserHabitsUseCase getUserHabitsUseCase;
    private final HabitRepository habitRepository;
    private final HabitApplicationMapper mapper;
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

}

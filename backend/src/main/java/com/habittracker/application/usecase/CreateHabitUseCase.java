package com.habittracker.application.usecase;

import com.habittracker.application.dto.command.CreateHabitCommand;
import com.habittracker.application.dto.response.HabitResponse;
import com.habittracker.application.exception.ApplicationException;
import com.habittracker.application.mapper.HabitApplicationMapper;
import com.habittracker.domain.entity.Habit;
import com.habittracker.domain.repository.HabitRepository;
import com.habittracker.domain.valueobject.HabitId;
import com.habittracker.domain.valueobject.UserId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateHabitUseCase {

    private final HabitRepository habitRepository;
    private final HabitApplicationMapper mapper;

    @Transactional
    public HabitResponse execute(CreateHabitCommand command) {
        log.info("Iniciando a criação de hábito para o usuário: {}", command.userId());

        var userId = UserId.from(command.userId());

        validateHabitNameUniqueness(userId, command.name());
        validateActiveHabitsLimit(userId);

        var habitId = HabitId.generate();
        var habit = new Habit(habitId, userId, command.name(), command.description());

        var savedHabit = habitRepository.save(habit);

        log.info("Hábito criado com sucesso: {} para usuário: {}", habitId, command.userId());

        return mapper.toResponse(savedHabit);
    }

    private void validateHabitNameUniqueness(UserId userId, String name) {
        if (habitRepository.existsByUserIdAndName(userId, name)) {
            throw new ApplicationException("Já existe um hábito com o nome '" + name + "' para este usuário.");
        }
    }

    private void validateActiveHabitsLimit(UserId userId) {
        long activeHabitsCount = habitRepository.countActiveByUserId(userId);
        if (activeHabitsCount >= 10) {
            throw new ApplicationException("Usuário já atingiu o limite máximo de 10 hábitos ativos.");
        }
    }
}

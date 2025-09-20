package com.habittracker.application.usecase;

import com.habittracker.application.dto.command.RecordHabitPracticeCommand;
import com.habittracker.application.dto.response.HabitResponse;
import com.habittracker.application.exception.ApplicationException;
import com.habittracker.application.exception.HabitNotFoundException;
import com.habittracker.application.mapper.HabitApplicationMapper;
import com.habittracker.domain.entity.Habit;
import com.habittracker.domain.repository.HabitRepository;
import com.habittracker.domain.valueobject.HabitId;
import com.habittracker.domain.valueobject.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecordHabitPracticeUseCase {

    private final HabitRepository habitRepository;
    private final HabitApplicationMapper mapper;

    public HabitResponse execute(RecordHabitPracticeCommand command) {
        log.info("Registrando prática de hábito: {} para usuário: {}", command.habitId(), command.userId());

        var habitId = HabitId.from(command.habitId());
        var userId = UserId.from(command.userId());

        var habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new HabitNotFoundException("Hábito com ID: '" + habitId + "' não encontrado."));

        validateHabitOwnership(habit, userId);

        habit.incrementStreak();

        var updatedHabit = habitRepository.save(habit);

        log.info("Prática registrada com sucesso. Nova sequência: {}", habit.getCurrentStreak());

        return mapper.toResponse(updatedHabit);
    }

    private void validateHabitOwnership(Habit habit, UserId requestingUser) {
        if (!habit.getUserId().equals(requestingUser)) {
            throw new ApplicationException("Usuário não tem permissão paraa modificar este hábito.");
        }
    }
}

package com.habittracker.application.usecase;

import com.habittracker.application.dto.command.ReactivateHabitCommand;
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

@Component
@Slf4j
@RequiredArgsConstructor
public class ReactivateHabitUseCase {

    private final HabitRepository habitRepository;
    private final HabitApplicationMapper mapper;

    public HabitResponse execute(ReactivateHabitCommand command) {
        log.info("Reativando hábito com ID: {} pelo usuário: {}", command.habitId(), command.userId());

        var habitId = HabitId.from(command.habitId());
        var userId = UserId.from(command.userId());

        var habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new HabitNotFoundException("Hábito com ID: '" + habitId + "' não encontrado."));

        validateHabitOwnership(habit, userId);

        validateActiveHabitsLimit(userId);

        habit.reactivate();

        var updatedHabit = habitRepository.save(habit);

        log.info("Hábito com ID: {} reativado com sucesso.", habitId);

        return mapper.toResponse(updatedHabit);
    }

    private void validateHabitOwnership(Habit habit, UserId requestingUser) {
        if (!habit.getUserId().equals(requestingUser)) {
            throw new ApplicationException("Usuário não tem permissão para modificar esse hábito.");
        }
    }

    private void validateActiveHabitsLimit(UserId userId) {
        var activeHabitsCount = habitRepository.countActiveByUserId(userId);
        if (activeHabitsCount >= 10) {
            throw new ApplicationException("Usuário atingiu o limite máximo de 10 hábitos ativos.");
        }
    }
}

package com.habittracker.application.usecase;

import com.habittracker.application.dto.command.BreakHabitStreakCommand;
import com.habittracker.application.dto.response.HabitResponse;
import com.habittracker.application.exception.ApplicationException;
import com.habittracker.application.exception.HabitNotFoundException;
import com.habittracker.application.mapper.HabitApplicationMapper;
import com.habittracker.domain.entity.Habit;
import com.habittracker.domain.repository.HabitRepository;
import com.habittracker.domain.valueobject.HabitId;
import com.habittracker.domain.valueobject.UserId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class BreakHabitStreakUseCase {

    private final HabitRepository habitRepository;
    private final HabitApplicationMapper mapper;

    @Transactional
    public HabitResponse execute(BreakHabitStreakCommand command) {
        log.info("Quebrando sequência do hábito: {} para usuário: {}", command.habitId(), command.userId());

        var habitId = HabitId.from(command.habitId());
        var userId = UserId.from(command.userId());

        var habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new HabitNotFoundException("Hábito com ID '" + habitId + "' não encontrado"));

        validateHabitOwnership(habit, userId);

        habit.breakStreak();

        var updatedHabit = habitRepository.save(habit);

        log.info("Sequência quebrada com sucesso para o hábito: {}", habitId);

        return mapper.toResponse(updatedHabit);
    }

    private void validateHabitOwnership(Habit habit, UserId requestingUser) {
        if (!habit.getUserId().equals(requestingUser)) {
            throw new ApplicationException("Usuário não tem permissão para modifcar esse hábito.");
        }
    }
}

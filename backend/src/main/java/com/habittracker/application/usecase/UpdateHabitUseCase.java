package com.habittracker.application.usecase;

import com.habittracker.application.dto.command.UpdateHabitCommand;
import com.habittracker.application.dto.response.HabitResponse;
import com.habittracker.application.exception.ApplicationException;
import com.habittracker.application.exception.HabitNotFoundException;
import com.habittracker.application.mapper.HabitApplicationMapper;
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
public class UpdateHabitUseCase {

    private final HabitRepository habitRepository;
    private final HabitApplicationMapper mapper;

    @Transactional
    public HabitResponse execute(UpdateHabitCommand command) {
        log.info("Iniciando a atualização de hábito: {} para usuário: {}", command.habitId(), command.userId());

        var habitId = HabitId.from(command.habitId());
        var userId = UserId.from(command.userId());

        var habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new HabitNotFoundException("Hábito com id '" + habitId + "' não encontrado"));

        validateHabitOwnership(habit.getUserId(), userId);

        if (!habit.getName().equals(command.name())) {
            validateHabitNameUniqueness(userId, command.name(), habitId);
        }

        habit.update(command.name(), command.description());
        habitRepository.save(habit);

        log.info("Hábito com ID: {} atualizado com sucesso pelo usuário: {}", command.habitId(), command.userId());

        return mapper.toResponse(habit);
    }

    private void validateHabitOwnership(UserId habitOwner, UserId requestingUser) {
        if (!habitOwner.equals(requestingUser)) {
            throw new ApplicationException("Usuário não tem permissão para modificar este hábito.");
        }
    }

    private void validateHabitNameUniqueness(UserId userId, String newHabitName, HabitId excludeHabitId) {
        var userHabits = habitRepository.findAllByUserId(userId);
        var nameExists = userHabits.stream()
                .anyMatch(h -> h.getName().equalsIgnoreCase(newHabitName) && !h.getId().equals(excludeHabitId));

        if (nameExists) {
            throw new ApplicationException("Já existe um hábito com o nome '" + newHabitName + "' para este usuário.");
        }
    }
}

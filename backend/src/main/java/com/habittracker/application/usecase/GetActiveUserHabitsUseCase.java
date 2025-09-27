package com.habittracker.application.usecase;

import com.habittracker.application.dto.response.HabitListResponse;
import com.habittracker.application.mapper.HabitApplicationMapper;
import com.habittracker.domain.repository.HabitRepository;
import com.habittracker.domain.valueobject.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class GetActiveUserHabitsUseCase {

    private final HabitRepository habitRepository;
    private final HabitApplicationMapper mapper;

    public HabitListResponse execute(String userIdStr) {
        log.info("Buscando hábitos ativos para o usuário: {}", userIdStr);

        var userId = UserId.from(userIdStr);
        var activeHabits = habitRepository.findActiveByUserId(userId);

        var habitResponses = activeHabits.stream()
                .map(mapper::toResponse)
                .toList();

        int totalCount = habitResponses.size();

        log.info("Encontrados {} hábitos ativos para usuário: {}", totalCount, userIdStr);

        return new HabitListResponse(habitResponses, totalCount, totalCount, 0);
    }
}

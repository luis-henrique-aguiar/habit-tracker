package com.habittracker.application.usecase;

import com.habittracker.application.dto.response.HabitListResponse;
import com.habittracker.application.dto.response.HabitResponse;
import com.habittracker.application.mapper.HabitApplicationMapper;
import com.habittracker.domain.repository.HabitRepository;
import com.habittracker.domain.valueobject.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class GetUserHabitsUseCase {

    private final HabitRepository habitRepository;
    private final HabitApplicationMapper mapper;

    public HabitListResponse execute(String userIdStr) {
        log.info("Buscando hábitos do usuário: {}", userIdStr);

        var userId = UserId.from(userIdStr);
        var habits = habitRepository.findAllByUserId(userId);

        var habitsResponse = habits.stream()
                .map(mapper::toResponse)
                .toList();

        int totalCount = habitsResponse.size();

        int activeHabits = (int) habitsResponse.stream()
                .filter(HabitResponse::active)
                .count();

        int inactiveHabits = totalCount - activeHabits;

        log.info("Encontrados {} hábitos para usuário: {} (ativos: {}, inativos: {}",
                totalCount, userIdStr, activeHabits, inactiveHabits);

        return new HabitListResponse(habitsResponse, totalCount, activeHabits, inactiveHabits);
    }
}

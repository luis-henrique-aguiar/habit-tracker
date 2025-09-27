package com.habittracker.application.usecase;

import com.habittracker.application.dto.response.UserHabitsStatisticsResponse;
import com.habittracker.application.mapper.HabitApplicationMapper;
import com.habittracker.domain.repository.HabitRepository;
import com.habittracker.domain.valueobject.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class GetUserHabitsStatisticsUseCase {

    private final HabitRepository habitRepository;
    private final HabitApplicationMapper mapper;

    public UserHabitsStatisticsResponse execute(String userIdStr) {
        log.info("Buscando estatísticas de hábitos do usuário: {}", userIdStr);

        var userId = UserId.from(userIdStr);
        var habits = habitRepository.findAllByUserId(userId);

        return mapper.toStatisticsResponse(userIdStr, habits);
    }
}

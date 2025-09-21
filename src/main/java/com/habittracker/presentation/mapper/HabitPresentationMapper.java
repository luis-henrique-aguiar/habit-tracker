package com.habittracker.presentation.mapper;

import com.habittracker.application.dto.command.CreateHabitCommand;
import com.habittracker.application.dto.command.UpdateHabitCommand;
import com.habittracker.application.dto.response.HabitListResponse;
import com.habittracker.application.dto.response.HabitResponse;
import com.habittracker.presentation.dto.request.CreateHabitRequest;
import com.habittracker.presentation.dto.response.HabitApiResponse;
import com.habittracker.presentation.dto.response.HabitListApiResponse;
import org.springframework.stereotype.Component;

@Component
public class HabitPresentationMapper {

    public CreateHabitCommand toCommand(CreateHabitRequest request, String userId) {
        return new CreateHabitCommand(
                userId,
                request.name(),
                request.description()
        );
    }

    public UpdateHabitCommand toCommand(UpdateHabitCommand request, String habitId, String userId) {
        return new UpdateHabitCommand(
                habitId,
                userId,
                request.name(),
                request.description()
        );
    }

    public HabitApiResponse toApiResponse(HabitResponse response) {
        return HabitApiResponse.from(
                response.id(),
                response.name(),
                response.description(),
                response.active(),
                response.currentStreak(),
                response.bestStreak(),
                response.createdAt(),
                response.isInGoodStreak(),
                response.isVeteran()
        );
    }

    public HabitListApiResponse toApiResponse(HabitListResponse response) {
        var habits = response.habits().stream()
                .map(this::toApiResponse)
                .toList();

        var metadata = new HabitListApiResponse.ListMetadata(
                response.totalCount(),
                response.activeCount(),
                response.inactiveCount(),
                calculateInGoodStreakCount(response)
        );

        return new HabitListApiResponse(habits, metadata);
    }

    private int calculateInGoodStreakCount(HabitListResponse response) {
        return (int) response.habits().stream()
                .filter(HabitResponse::isInGoodStreak)
                .count();
    }
}

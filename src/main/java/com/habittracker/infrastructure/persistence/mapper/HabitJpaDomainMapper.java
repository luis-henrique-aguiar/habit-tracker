package com.habittracker.infrastructure.persistence.mapper;

import com.habittracker.domain.entity.Habit;
import com.habittracker.domain.valueobject.*;
import com.habittracker.infrastructure.persistence.entity.HabitJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class HabitJpaDomainMapper {

    public Habit toDomain(HabitJpaEntity jpaEntity) {
        if (jpaEntity == null) {
            return null;
        }

        var habitId = new HabitId(jpaEntity.getId());
        var userId = new UserId(jpaEntity.getUserId());

        var basicInformation = new BasicInformation(
            jpaEntity.getName(),
            jpaEntity.getDescription()
        );

        var progressStatistics = new ProgressStatistics(
            jpaEntity.getCurrentStreak(),
            jpaEntity.getBestStreak()
        );

        var lifecycleState = new LifecycleState(
            jpaEntity.getActive(),
            jpaEntity.getCreatedAt()
        );

        return new Habit(habitId, userId, basicInformation, progressStatistics, lifecycleState);
    }

    public HabitJpaEntity toJpaEntity(Habit domainEntity) {
        if (domainEntity == null) {
            return null;
        }

        return HabitJpaEntity.builder()
                .id(domainEntity.getId().value())
                .userId(domainEntity.getUserId().value())
                .name(domainEntity.getName())
                .description(domainEntity.getDescription())
                .active(domainEntity.isActive())
                .currentStreak(domainEntity.getStatistics().bestStreak())
                .bestStreak(domainEntity.getStatistics().bestStreak())
                .createdAt(domainEntity.getState().createdAt())
                .build();
    }

    public void updateJpaEntity(HabitJpaEntity jpaEntity, Habit domainEntity) {
        if (jpaEntity == null || domainEntity == null) {
            return;
        }

        jpaEntity.setName(domainEntity.getInformation().name());
        jpaEntity.setDescription(domainEntity.getInformation().description());
        jpaEntity.setActive(domainEntity.getState().active());
        jpaEntity.setCurrentStreak(domainEntity.getStatistics().currentStreak());
        jpaEntity.setBestStreak(domainEntity.getStatistics().bestStreak());
    }

}

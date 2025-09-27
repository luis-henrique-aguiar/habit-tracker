package com.habittracker.infrastructure.persistence.repository;

import com.habittracker.infrastructure.persistence.entity.HabitJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HabitJpaRepository extends JpaRepository<HabitJpaEntity, UUID> {

    List<HabitJpaEntity> findByUserId(UUID userId);

    List<HabitJpaEntity> findByUserIdAndActiveTrue(UUID userId);

    boolean existsByUserIdAndName(UUID userId, String name);

    long countByUserIdAndActiveTrue(UUID userId);

    List<HabitJpaEntity> findByUserIdOrderByCurrentStreakDesc(UUID userId);

    @Query("""
        SELECT h FROM HabitJpaEntity h
        WHERE h.userId = :userId
        AND h.createdAt <= CURRENT_TIMESTAMP - 30 DAY
        ORDER BY h.createdAt ASC
    """)
    List<HabitJpaEntity> findVeteranHabitsByUserId(@Param("userId") UUID userId);

    @Query("""
        SELECT h from HabitJpaEntity h
        WHERE h.userId = :userId
        AND h.active = true
        AND h.currentStreak >= 7
        ORDER BY h.currentStreak DESC
    """)
    List<HabitJpaEntity> findHabitsInGoodStreakByUserId(@Param("userId") UUID userId);

    @Query(value = """
        SELECT
            COUNT(*) as total_habits
            COUNT(CASE WHEN active = true THEN 1 END) as active_habits
            COUNT(CASE WHEN current_streak >= 7 AND active = true THEN 1 END) as good_streak_habits
            COALESCE(AVG(CASE WHEN active = true THEN current_streak END), 0) as avg_current_streak
            COALESCE(MAX(best_streak), 0) as max_best_streak
        FROM habits
        WHERE user_id = :userId
    """, nativeQuery = true)
    HabitStatisticsProjection getHabitsStatisticsByUserId(@Param("userId") UUID userId);

    interface HabitStatisticsProjection {
        Long getTotalHabits();
        Long getActiveHabits();
        Long getGoodStreakHabits();
        Double getAvgCurrentStreak();
        Integer getMaxBestStreak();
    }
}

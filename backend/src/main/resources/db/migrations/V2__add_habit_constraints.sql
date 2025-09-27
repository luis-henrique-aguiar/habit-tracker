ALTER TABLE habits ADD CONSTRAINT chk_habits_current_streak_positive
    CHECK (current_streak >= 0);

ALTER TABLE habits ADD CONSTRAINT chk_habits_best_streak_positive
    CHECK (best_streak >= 0);

ALTER TABLE habits ADD CONSTRAINT chk_habits_best_streak_gte_current
    CHECK (best_streak >= current_streak);

ALTER TABLE habits ADD CONSTRAINT chk_habits_name_not_empty
    CHECK (LENGTH(TRIM(name)) > 0);
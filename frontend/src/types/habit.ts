export interface CreateHabitRequest {
  name: string;
  description?: string;
}

export interface UpdateHabitRequest {
  name: string;
  description?: string;
}

export interface Habit {
  id: string;
  name: string;
  description?: string;
  active: boolean;
  current_streak: number;
  best_streak: number;
  created_at: string;
  is_in_good_streak: boolean;
  is_veteran: boolean;
  streak_status: string;
}

export interface HabitListResponse {
  habits: Habit[],
  metadate: {
    total: number;
    active: number;
    inactive: number;
    inGoodStreak: number;
  }
}

export interface SuccessResponse {
  status: string;
  message: string;
  timestamp: string;
}
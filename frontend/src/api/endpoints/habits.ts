import type {
  CreateHabitRequest,
  Habit,
  HabitListResponse,
  SuccessResponse,
  UpdateHabitRequest,
} from "../../types/habit";
import { apiClient } from "../client";

export const habitsApi = {
  async getHabits(userId: string): Promise<HabitListResponse> {
    const { data } = await apiClient.get(`/users/${userId}/habits`);
    return data;
  },

  async createHabit(userId: string, habit: CreateHabitRequest): Promise<Habit> {
    const { data } = await apiClient.post(`/users/${userId}/habits`, habit);
    return data;
  },

  async updateHabit(
    userId: string,
    habitId: string,
    habit: UpdateHabitRequest
  ): Promise<Habit> {
    const { data } = await apiClient.put(
      `/users/${userId}/habits/${habitId}`,
      habit
    );
    return data;
  },

  async practiceHabit(
    userId: string,
    habitId: string
  ): Promise<SuccessResponse> {
    const { data } = await apiClient.post(
      `/users/${userId}/habits/${habitId}/practice`
    );
    return data;
  },

  async deleteHabit(userId: string, habitId: string): Promise<SuccessResponse> {
    const { data } = await apiClient.delete(
      `/users/${userId}/habits/${habitId}`
    );
    return data;
  },
};

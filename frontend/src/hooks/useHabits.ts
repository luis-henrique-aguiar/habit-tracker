import { habitsApi } from "@/api/endpoints/habits";
import { useAuthStore } from "@/store/authStore";
import {
  type HabitListResponse,
  type Habit,
  type CreateHabitRequest,
  type UpdateHabitRequest,
} from "@/types/habit";
import { useCallback, useEffect, useState } from "react";
import toast from "react-hot-toast";

export function useHabits() {
  const user = useAuthStore((state) => state.user);
  const [habits, setHabits] = useState<Habit[]>([]);
  const [metadata, setMetadata] = useState<
    HabitListResponse["metadata"] | null
  >(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchHabits = useCallback(async () => {
    if (!user?.id) return;

    setIsLoading(true);
    setError(null);

    try {
      const response = await habitsApi.getHabits(user.id);
      setHabits(response.habits);
      setMetadata(response.metadata);
    } catch (err) {
      setError("Erro ao carregar hábitos.");
      console.error(err);
    } finally {
      setIsLoading(false);
    }
  }, [user?.id]);

  const createHabit = async (data: CreateHabitRequest) => {
    if (!user?.id) return;

    try {
      const newHabit = await habitsApi.createHabit(user.id, data);
      setHabits((prev) => [...prev, newHabit]);
      toast.success("Hábito criado com sucesso.");
      return newHabit;
    } catch (err) {
      console.error(err);
      throw err;
    }
  };

  const updateHabit = async (habitId: string, data: UpdateHabitRequest) => {
    if (!user?.id) return;

    try {
      const updatedHabit = await habitsApi.updateHabit(user.id, habitId, data);
      setHabits((prev) =>
        prev.map((h) => (h.id === habitId ? updatedHabit : h))
      );
      toast.success("Hábito atualizado com sucesso!");
      return updatedHabit;
    } catch (err) {
      console.error(err);
      throw err;
    }
  };

  const practiceHabit = async (habitId: string) => {
    if (!user?.id) return;

    try {
      await habitsApi.practiceHabit(user.id, habitId);
      await fetchHabits();
      toast.success("Prática registrada! 🎯");
    } catch (err) {
      console.error(err);
      throw err;
    }
  };

  const deleteHabit = async (habitId: string) => {
    if (!user?.id) return;

    try {
      await habitsApi.deleteHabit(user.id, habitId);
      setHabits((prev) => prev.filter((h) => h.id !== habitId));
      toast.success("Hábito removido");
    } catch (err) {
      console.error(err);
      throw err;
    }
  };

  useEffect(() => {
    fetchHabits();
  }, [fetchHabits]);

  return {
    habits,
    metadata,
    isLoading,
    error,
    createHabit,
    updateHabit,
    practiceHabit,
    deleteHabit,
    refetch: fetchHabits,
  };
}

import type { Habit } from "@/types/habit";
import { useState } from "react";
import {
  Check,
  Edit,
  Flame,
  MoreVertical,
  Trash2,
  TrendingUp,
  Trophy,
} from "lucide-react";

interface HabitCardProps {
  habit: Habit;
  onPractice: (id: string) => Promise<void>;
  onEdit: (habit: Habit) => void;
  onDelete: (id: string) => Promise<void>;
}

export function HabitCard({
  habit,
  onPractice,
  onEdit,
  onDelete,
}: HabitCardProps) {
  const [isLoading, setIsLoading] = useState(false);
  const [showMenu, setShowMenu] = useState(false);

  const handlePractice = async () => {
    setIsLoading(true);
    try {
      await onPractice(habit.id);
    } finally {
      setIsLoading(false);
    }
  };

  const handleDelete = async () => {
    if (confirm(`Tem certeza que deseja excluir "${habit.name}"`)) {
      await onDelete(habit.id);
    }
  };

  const getStreakColor = () => {
    if (habit.current_streak >= 30) return "text-orange-500";
    if (habit.current_streak >= 7) return "text-green-500";
    if (habit.current_streak >= 3) return "text-blue-500";
    return "text-gray-500";
  };

  return (
    <>
      <div className="card relative hover:shadow-md transition-shadow duration-200">
        {/* Menu de ações */}
        <div className="absolute top-4 right-4">
          <button
            onClick={() => setShowMenu(!showMenu)}
            className="p-2 hover:bg-gray-100 rounded-lg transition-colors"
          >
            <MoreVertical className="h-5 w-5 text-gray-500" />
          </button>

          {showMenu && (
            <div className="absolute right-0 mt-2 w-48 bg-white rounded-lg shadow-lg border border-gray-100 z-10">
              <button
                onClick={() => {
                  onEdit(habit);
                  setShowMenu(false);
                }}
                className="w-full flex items-center gap-2 px-4 py-2 hover:bg-gray-50 text-left"
              >
                <Edit className="h-4 w-4" />
                Editar
              </button>
              <button
                onClick={() => {
                  handleDelete();
                  setShowMenu(false);
                }}
                className="w-full flex items-center gap-2 px-4 py-2 hover:bg-gray-50 text-left text-red-600"
              >
                <Trash2 className="h-4 w-4" />
                Excluir
              </button>
            </div>
          )}
        </div>

        {/* Conteúdo do card */}
        <div className="mb-4">
          <h3 className="text-lg font-semibold text-gray-900 mb-1">
            {habit.name}
          </h3>
          {habit.description && (
            <p className="text-sm text-gray-600">{habit.description}</p>
          )}
        </div>

        {/* Estatísticas */}
        <div className="grid grid-cols-3 gap-4 mb-4">
          <div className="text-center">
            <div
              className={`flex items-center justify-center mb-1 ${getStreakColor()}`}
            >
              <Flame className="h-5 w-5 mr-1" />
              <span className="text-xl font-bold">{habit.current_streak}</span>
            </div>
            <p className="text-xs text-gray-500">Sequência Atual</p>
          </div>

          <div className="text-center">
            <div className="flex items-center justify-center mb-1 text-purple-500">
              <Trophy className="h-5 w-5 mr-1" />
              <span className="text-xl font-bold">{habit.best_streak}</span>
            </div>
            <p className="text-xs text-gray-500">Melhor Sequência</p>
          </div>

          <div className="text-center">
            <div className="flex items-center justify-center mb-1">
              {habit.is_veteran ? (
                <span className="text-yellow-500">⭐</span>
              ) : (
                <TrendingUp className="h-5 w-5 text-gray-400" />
              )}
            </div>
            <p className="text-xs text-gray-500">
              {habit.is_veteran ? "Veterano" : "Em progresso"}
            </p>
          </div>
        </div>

        {/* Status da sequência */}
        <div className="mb-4 p-3 bg-gray-50 rounded-lg">
          <p className="text-sm font-medium text-gray-700">
            {habit.streak_status}
          </p>
        </div>

        {/* Botão de prática */}
        <button
          onClick={handlePractice}
          disabled={isLoading || !habit.active}
          className={`w-full flex items-center justify-center gap-2 py-3 rounded-lg font-medium transition-all duration-200
          ${
            habit.active
              ? "bg-primary-600 text-white hover:bg-primary-700"
              : "bg-gray-200 text-gray-500 cursor-not-allowed"
          }`}
        >
          {isLoading ? (
            <div className="animate-spin rounded-full h-5 w-5 border-2 border-white border-t-transparent" />
          ) : (
            <>
              <Check className="h-5 w-5" />
              Marcar como Feito
            </>
          )}
        </button>
      </div>
    </>
  );
}

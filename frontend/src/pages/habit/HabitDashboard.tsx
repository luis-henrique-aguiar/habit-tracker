import { useState } from "react";
import { Plus, Target, TrendingUp, Award, Activity } from "lucide-react";
import { useHabits } from "@/hooks/useHabits";
import { HabitCard } from "@/components/habits/HabitCard";
import { HabitForm } from "@/components/habits/HabitForm";
import { Modal } from "@/components/common/Modal";
import { LoadingSpinner } from "@/components/common/LoadingSpinner";
import type { Habit } from "@/types/habit";

export function HabitDashboard() {
  const {
    habits,
    metadata,
    isLoading,
    createHabit,
    updateHabit,
    practiceHabit,
    deleteHabit,
  } = useHabits();
  const [showForm, setShowForm] = useState(false);
  const [editingHabit, setEditingHabit] = useState<Habit | null>(null);

  const handleSubmit = async (data: any) => {
    if (editingHabit) {
      await updateHabit(editingHabit.id, data);
    } else {
      await createHabit(data);
    }
  };

  const openCreateForm = () => {
    setEditingHabit(null);
    setShowForm(true);
  };

  const openEditForm = (habit: Habit) => {
    setEditingHabit(habit);
    setShowForm(true);
  };

  const closeForm = () => {
    setShowForm(false);
    setEditingHabit(null);
  };

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-96">
        <LoadingSpinner />
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto px-4 py-8">
      {/* Header com estatísticas */}
      <div className="mb-8">
        <div className="flex items-center justify-between mb-6">
          <div>
            <h1 className="text-3xl font-bold text-gray-900">Meus Hábitos</h1>
            <p className="text-gray-600 mt-1">
              Acompanhe seus hábitos e construa uma vida melhor
            </p>
          </div>
          <button
            onClick={openCreateForm}
            className="btn-primary flex items-center gap-2"
          >
            <Plus className="h-5 w-5" />
            Novo Hábito
          </button>
        </div>

        {/* Cards de estatísticas */}
        {metadata && (
          <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mb-8">
            <div className="card flex items-center gap-4">
              <div className="p-3 bg-blue-100 rounded-lg">
                <Target className="h-6 w-6 text-blue-600" />
              </div>
              <div>
                <p className="text-2xl font-bold text-gray-900">
                  {metadata.total}
                </p>
                <p className="text-sm text-gray-600">Total de Hábitos</p>
              </div>
            </div>

            <div className="card flex items-center gap-4">
              <div className="p-3 bg-green-100 rounded-lg">
                <Activity className="h-6 w-6 text-green-600" />
              </div>
              <div>
                <p className="text-2xl font-bold text-gray-900">
                  {metadata.active}
                </p>
                <p className="text-sm text-gray-600">Hábitos Ativos</p>
              </div>
            </div>

            <div className="card flex items-center gap-4">
              <div className="p-3 bg-orange-100 rounded-lg">
                <TrendingUp className="h-6 w-6 text-orange-600" />
              </div>
              <div>
                <p className="text-2xl font-bold text-gray-900">
                  {metadata.inGoodStreak}
                </p>
                <p className="text-sm text-gray-600">Em Boa Sequência</p>
              </div>
            </div>

            <div className="card flex items-center gap-4">
              <div className="p-3 bg-purple-100 rounded-lg">
                <Award className="h-6 w-6 text-purple-600" />
              </div>
              <div>
                <p className="text-2xl font-bold text-gray-900">
                  {habits.filter((h) => h.is_veteran).length}
                </p>
                <p className="text-sm text-gray-600">Veteranos (30+ dias)</p>
              </div>
            </div>
          </div>
        )}
      </div>

      {/* Lista de hábitos */}
      {habits.length === 0 ? (
        <div className="text-center py-12">
          <div className="mb-4">
            <Target className="h-16 w-16 text-gray-300 mx-auto" />
          </div>
          <h3 className="text-lg font-medium text-gray-900 mb-2">
            Nenhum hábito cadastrado
          </h3>
          <p className="text-gray-600 mb-4">
            Comece sua jornada criando seu primeiro hábito!
          </p>
          <button
            onClick={openCreateForm}
            className="btn-primary inline-flex items-center gap-2"
          >
            <Plus className="h-5 w-5" />
            Criar Primeiro Hábito
          </button>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {habits.map((habit) => (
            <HabitCard
              key={habit.id}
              habit={habit}
              onPractice={practiceHabit}
              onEdit={openEditForm}
              onDelete={deleteHabit}
            />
          ))}
        </div>
      )}

      {/* Modal do formulário */}
      <Modal
        isOpen={showForm}
        onClose={closeForm}
        title={editingHabit ? "Editar Hábito" : "Novo Hábito"}
      >
        <HabitForm
          habit={editingHabit}
          onSubmit={handleSubmit}
          onCancel={closeForm}
        />
      </Modal>
    </div>
  );
}

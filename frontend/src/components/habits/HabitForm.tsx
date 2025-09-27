import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import type { CreateHabitRequest, Habit } from "@/types/habit";

const habitSchema = z.object({
  name: z
    .string()
    .min(1, "Nome é obrigatório")
    .max(100, "Nome não pode ter mais de 100 caracteres"),
  description: z
    .string()
    .max(500, "Descrição não pode ter mais de 500 caracteres")
    .optional(),
});

interface HabitFormProps {
  habit?: Habit | null;
  onSubmit: (data: CreateHabitRequest) => Promise<void>;
  onCancel: () => void;
}

export function HabitForm({ habit, onSubmit, onCancel }: HabitFormProps) {
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<CreateHabitRequest>({
    resolver: zodResolver(habitSchema),
    defaultValues: {
      name: habit?.name || "",
      description: habit?.description || "",
    },
  });

  const onFormSubmit = async (data: CreateHabitRequest) => {
    try {
      await onSubmit(data);
      onCancel(); // Fecha o modal após sucesso
    } catch (error) {
      console.error("Erro ao salvar hábito:", error);
    }
  };

  return (
    <form onSubmit={handleSubmit(onFormSubmit)} className="space-y-4">
      <div>
        <label
          htmlFor="name"
          className="block text-sm font-medium text-gray-700 mb-1"
        >
          Nome do Hábito *
        </label>
        <input
          id="name"
          type="text"
          {...register("name")}
          className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 
                   focus:ring-primary-500 focus:border-transparent transition-colors"
          placeholder="Ex: Meditar"
        />
        {errors.name && (
          <p className="mt-1 text-sm text-red-600">{errors.name.message}</p>
        )}
      </div>

      <div>
        <label
          htmlFor="description"
          className="block text-sm font-medium text-gray-700 mb-1"
        >
          Descrição
        </label>
        <textarea
          id="description"
          {...register("description")}
          rows={3}
          className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 
                   focus:ring-primary-500 focus:border-transparent transition-colors resize-none"
          placeholder="Ex: 10 minutos de meditação mindfulness"
        />
        {errors.description && (
          <p className="mt-1 text-sm text-red-600">
            {errors.description.message}
          </p>
        )}
      </div>

      <div className="flex gap-3 pt-4">
        <button
          type="submit"
          disabled={isSubmitting}
          className="flex-1 btn-primary"
        >
          {isSubmitting ? "Salvando..." : habit ? "Atualizar" : "Criar Hábito"}
        </button>
        <button
          type="button"
          onClick={onCancel}
          disabled={isSubmitting}
          className="flex-1 btn-secondary"
        >
          Cancelar
        </button>
      </div>
    </form>
  );
}

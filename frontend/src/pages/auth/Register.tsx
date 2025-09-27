import { useAuthStore } from "@/store/authStore";
import { zodResolver } from "@hookform/resolvers/zod";
import { Eye, EyeOff, UserPlus } from "lucide-react";
import { useState } from "react";
import { useForm } from "react-hook-form";
import toast from "react-hot-toast";
import { Link, useNavigate } from "react-router-dom";
import z from "zod";

const registerSchema = z
  .object({
    name: z.string().min(3, "Nome deve ter no mínimo 3 caracteres"),
    email: z.string().email("Email inválido"),
    password: z.string().min(6, "Senha deve ter no mínimo 6 caracteres"),
    confirmPassword: z.string(),
  })
  .refine((data) => data.password === data.confirmPassword, {
    message: "As senhas não coincidem",
    path: ["confirmPassword"],
  });

type RegisterForm = z.infer<typeof registerSchema>;

export function Register() {
  const navigate = useNavigate();
  const register = useAuthStore((state) => state.register);
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);

  const {
    register: registerField,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<RegisterForm>({
    resolver: zodResolver(registerSchema),
  });

  const onSubmit = async (data: RegisterForm) => {
    try {
      await register(data.name, data.email, data.password);
      toast.success("Conta criada com sucesso!");
      navigate("/dashboard");
    } catch (error) {
      console.error(error);
      toast.error("Erro ao criar conta. Tente novamente.");
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-primary-50 to-primary-100 px-4">
      <div className="w-full max-w-md">
        <div className="card">
          {/* Header */}
          <div className="text-center mb-8">
            <div
              className="inline-flex items-center justify-center w-16 h-16 bg-primary-600 
                          rounded-full mb-4"
            >
              <UserPlus className="h-8 w-8 text-white" />
            </div>
            <h2 className="text-2xl font-bold text-gray-900">Criar conta</h2>
            <p className="text-gray-600 mt-2">
              Comece sua jornada de hábitos hoje
            </p>
          </div>

          {/* Formulário */}
          <form onSubmit={handleSubmit(onSubmit)} className="space-y-5">
            <div>
              <label
                htmlFor="name"
                className="block text-sm font-medium text-gray-700 mb-1"
              >
                Nome completo
              </label>
              <input
                id="name"
                type="text"
                {...registerField("name")}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 
                         focus:ring-primary-500 focus:border-transparent transition-colors"
                placeholder="João Silva"
              />
              {errors.name && (
                <p className="mt-1 text-sm text-red-600">
                  {errors.name.message}
                </p>
              )}
            </div>

            <div>
              <label
                htmlFor="email"
                className="block text-sm font-medium text-gray-700 mb-1"
              >
                Email
              </label>
              <input
                id="email"
                type="email"
                {...registerField("email")}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 
                         focus:ring-primary-500 focus:border-transparent transition-colors"
                placeholder="seu@email.com"
              />
              {errors.email && (
                <p className="mt-1 text-sm text-red-600">
                  {errors.email.message}
                </p>
              )}
            </div>

            <div>
              <label
                htmlFor="password"
                className="block text-sm font-medium text-gray-700 mb-1"
              >
                Senha
              </label>
              <div className="relative">
                <input
                  id="password"
                  type={showPassword ? "text" : "password"}
                  {...registerField("password")}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 
                           focus:ring-primary-500 focus:border-transparent transition-colors pr-10"
                  placeholder="••••••••"
                />
                <button
                  type="button"
                  onClick={() => setShowPassword(!showPassword)}
                  className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-500 
                           hover:text-gray-700 transition-colors"
                >
                  {showPassword ? (
                    <EyeOff className="h-4 w-4" />
                  ) : (
                    <Eye className="h-4 w-4" />
                  )}
                </button>
              </div>
              {errors.password && (
                <p className="mt-1 text-sm text-red-600">
                  {errors.password.message}
                </p>
              )}
            </div>

            <div>
              <label
                htmlFor="confirmPassword"
                className="block text-sm font-medium text-gray-700 mb-1"
              >
                Confirmar senha
              </label>
              <div className="relative">
                <input
                  id="confirmPassword"
                  type={showConfirmPassword ? "text" : "password"}
                  {...registerField("confirmPassword")}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 
                           focus:ring-primary-500 focus:border-transparent transition-colors pr-10"
                  placeholder="••••••••"
                />
                <button
                  type="button"
                  onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                  className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-500 
                           hover:text-gray-700 transition-colors"
                >
                  {showConfirmPassword ? (
                    <EyeOff className="h-4 w-4" />
                  ) : (
                    <Eye className="h-4 w-4" />
                  )}
                </button>
              </div>
              {errors.confirmPassword && (
                <p className="mt-1 text-sm text-red-600">
                  {errors.confirmPassword.message}
                </p>
              )}
            </div>

            <button
              type="submit"
              disabled={isSubmitting}
              className="w-full btn-primary py-3"
            >
              {isSubmitting ? "Criando conta..." : "Criar conta"}
            </button>
          </form>

          {/* Link para login */}
          <div className="mt-6 text-center">
            <p className="text-sm text-gray-600">
              Já tem uma conta?{" "}
              <Link
                to="/login"
                className="text-primary-600 hover:text-primary-700 font-medium"
              >
                Fazer login
              </Link>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}

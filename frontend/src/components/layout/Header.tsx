import { useNavigate } from "react-router-dom";
import { LogOut, User, Target } from "lucide-react";
import { useAuthStore } from "@/store/authStore";

export function Header() {
  const navigate = useNavigate();
  const { user, logout } = useAuthStore();

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <header className="bg-white border-b border-gray-200">
      <div className="max-w-7xl mx-auto px-4">
        <div className="flex items-center justify-between h-16">
          {/* Logo */}
          <div className="flex items-center gap-2">
            <Target className="h-8 w-8 text-primary-600" />
            <span className="text-xl font-bold text-gray-900">
              HabitTracker
            </span>
          </div>

          {/* User menu */}
          <div className="flex items-center gap-4">
            <div className="flex items-center gap-2">
              <User className="h-5 w-5 text-gray-500" />
              <span className="text-sm font-medium text-gray-700">
                {user?.name}
              </span>
            </div>
            <button
              onClick={handleLogout}
              className="flex items-center gap-2 px-3 py-2 text-sm text-gray-600 
                       hover:text-gray-900 transition-colors"
            >
              <LogOut className="h-4 w-4" />
              Sair
            </button>
          </div>
        </div>
      </div>
    </header>
  );
}

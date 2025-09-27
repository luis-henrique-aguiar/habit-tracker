import { Navigate, Route, BrowserRouter, Routes } from "react-router-dom";
import { ProtectedRoute } from "./components/ProtectedRoute";
import { Layout } from "lucide-react";
import { Login } from "./pages/auth/Login";
import { HabitDashboard } from "./pages/habit/HabitDashboard";
import { Toaster } from "react-hot-toast";
import { NotFound } from "./pages/NotFound";
import { Register } from "./pages/auth/Register";

function App() {
  return (
    <BrowserRouter>
      <Toaster
        position="top-right"
        toastOptions={{
          duration: 4000,
          style: {
            background: "#363636",
            color: "#fff",
          },
          success: {
            style: {
              background: "#10b981",
            },
          },
          error: {
            style: {
              background: "#ef4444",
            },
          },
        }}
      />

      <Routes>
        {/* Rotas públicas */}
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

        {/* Rotas protegidas */}
        <Route element={<ProtectedRoute />}>
          <Route element={<Layout />}>
            <Route path="/dashboard" element={<HabitDashboard />} />
            <Route path="/habits" element={<HabitDashboard />} />
          </Route>
        </Route>

        {/* Redirects */}
        <Route path="/" element={<Navigate to="/dashboard" replace />} />
        <Route path="*" element={<NotFound />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;

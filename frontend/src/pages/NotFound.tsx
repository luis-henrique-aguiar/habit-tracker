import { AlertCircle, Home } from "lucide-react";
import { Link } from "react-router-dom";

export function NotFound() {
  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50 px-4">
      <div className="text-center">
        <div
          className="inline-flex items-center justify-center w-20 h-20 bg-red-100 
                      rounded-full mb-6"
        >
          <AlertCircle className="h-10 w-10 text-red-600" />
        </div>

        <h1 className="text-6xl font-bold text-gray-900 mb-4">404</h1>
        <h2 className="text-2xl font-semibold text-gray-700 mb-4">
          Página não encontrada
        </h2>
        <p className="text-gray-600 mb-8 max-w-md mx-auto">
          Ops! A página que você está procurando não existe ou foi movida.
        </p>

        <Link to="/" className="btn-primary inline-flex items-center gap-2">
          <Home className="h-5 w-5" />
          Voltar ao início
        </Link>
      </div>
    </div>
  );
}

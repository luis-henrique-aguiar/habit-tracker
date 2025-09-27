import axios, { AxiosError, type InternalAxiosRequestConfig } from "axios";
import toast from "react-hot-toast";

const API_URL = import.meta.env.VITE_API_URL || "http://localhost:8080/api/v1";

export const apiClient = axios.create({
  baseURL: API_URL,
  timeout: 10000,
  headers: {
    "Content-Type": "application/json",
  },
});

apiClient.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    const token = localStorage.getItem("authToken");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error: AxiosError) => {
    return Promise.reject(error);
  }
);

apiClient.interceptors.response.use(
  (response) => response,
  (error: AxiosError<ErrorApiResponse>) => {
    if (error.response) {
      const { status, data } = error.response;

      switch (status) {
        case 401:
          localStorage.removeItem("authToken");
          window.location.href = "/login";
          toast.error("Sessão expierada. Faça login novamente.");
          break;
        case 403:
          toast.error("Você não tem permissão paara esta ação.");
          break;
        case 404:
          toast.error(error.message || "Rescurso não encontrado.");
          break;
        case 422:
        case 400:
          if (data.details && data.details.length > 0) {
            data.details.forEach((detail) => toast.error(detail));
          } else {
            toast.error(data.message || "Dados inválidos");
          }
          break;
        case 500:
          toast.error("Erro no servidor. Tente novamente mais tarde.");
          break;
        default:
          toast.error("Ocorreu um erro. Tente novamente");
      }
    } else if (error.request) {
      toast.error("Erro de conexão. Verifique a sua internet.");
    } else {
      toast.error("Erro ao processar requisição.");
    }

    return Promise.reject(error);
  }
);

interface ErrorApiResponse {
  status: number;
  error: string;
  message: string;
  details?: string[];
  path: string;
  timestamp: string;
  traceId: string;
}

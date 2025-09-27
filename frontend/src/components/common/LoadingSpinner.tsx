export function LoadingSpinner() {
  return (
    <div className="flex flex-col items-center gap-3">
      <div className="relative h-12 w-12">
        <div className="absolute inset-0 animate-spin rounded-full border-4 border-gray-200"></div>
        <div className="absolute inset-0 animate-spin rounded-full border-4 border-primary-600 border-t-transparent"></div>
      </div>
      <p className="text-sm text-gray-600">Carregando...</p>
    </div>
  );
}

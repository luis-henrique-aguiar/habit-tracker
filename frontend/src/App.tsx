function App() {
  return (
    <>
      <div className="min-h-screen p-8 bg-gray-50 dark:bg-[rgb(var(--color-background))]">
        <header className="mb-8 flex items-center justify-between">
          <h1 className="text-3xl font-bold text-primary">Habit Tracker</h1>
          <div className="flex gap-3">
            <button className="btn-secondary">Profile</button>
            <button className="btn-primary">New Habit</button>
          </div>
        </header>

        <main className="grid gap-6 grid-cols-1 md:grid-cols-2 lg:grid-cols-3">
          <article className="card fadeIn">
            <h2 className="text-xl font-semibold text-primary">Read 30 min</h2>
            <p className="mt-2 text-sm text-gray-600">
              Daily reading habit to improve knowledge.
            </p>
            <div className="mt-4 flex items-center gap-3">
              <button className="btn-primary">Mark done</button>
              <button className="btn-secondary">Edit</button>
            </div>
          </article>

          <article className="card animate-fadeIn">
            <h2 className="text-xl font-semibold text-primary">Read 30 min</h2>
            <p className="mt-2 text-sm text-gray-600">
              Daily reading habit to improve knowledge.
            </p>
            <div className="mt-4 flex items-center gap-3">
              <button className="btn-primary">Mark done</button>
              <button className="btn-secondary">Edit</button>
            </div>
          </article>

          <article className="card animate-fadeIn">
            <h2 className="text-xl font-semibold text-primary">Read 30 min</h2>
            <p className="mt-2 text-sm text-gray-600">
              Daily reading habit to improve knowledge.
            </p>
            <div className="mt-4 flex items-center gap-3">
              <button className="btn-primary">Mark done</button>
              <button className="btn-secondary">Edit</button>
            </div>
          </article>

          <div className="card">
            <h3 className="text-lg font-medium text-primary">Theme demo</h3>
            <div className="mt-3 flex gap-2">
              <div className="w-10 h-10 rounded bg-primary" />
              <div className="w-10 h-10 rounded bg-[rgb(var(--color-primary-300))]" />
              <div className="w-10 h-10 rounded bg-[rgb(var(--color-primary-700))]" />
            </div>
          </div>
        </main>
      </div>
    </>
  );
}

export default App;

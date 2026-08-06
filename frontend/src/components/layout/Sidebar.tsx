export type AppPage = "analysis" | "history";

interface SidebarProps {
  activePage: AppPage;
  onNavigate: (page: AppPage) => void;
}

export function Sidebar({ activePage, onNavigate }: SidebarProps) {
  return (
    <aside className="sidebar">
      <div className="brand">
        <div className="brand-icon" aria-hidden="true">
          A
        </div>

        <div>
          <strong>Hub</strong>
          <span>Validador WCAG</span>
        </div>
      </div>

      <nav className="main-navigation" aria-label="Navegação principal">
        <button
          type="button"
          className={
            activePage === "analysis"
              ? "navigation-button active"
              : "navigation-button"
          }
          aria-current={activePage === "analysis" ? "page" : undefined}
          onClick={() => onNavigate("analysis")}
        >
          <span aria-hidden="true">＋</span>
          Nova análise
        </button>

        <button
          type="button"
          className={
            activePage === "history"
              ? "navigation-button active"
              : "navigation-button"
          }
          aria-current={activePage === "history" ? "page" : undefined}
          onClick={() => onNavigate("history")}
        >
          <span aria-hidden="true">◷</span>
          Histórico
        </button>
      </nav>

      <div className="sidebar-footer">
        <p>Desenvolvido com foco em acessibilidade digital.</p>
      </div>
    </aside>
  );
}

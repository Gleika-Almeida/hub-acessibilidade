export function Sidebar() {
  return (
    <aside className="sidebar">
      <div className="brand">
        <div className="bran-icon" aria-hidden="true">
          A
        </div>
        <div>
          <strong>A11Y Hub</strong>
          <span>Validador WCAG</span>
        </div>
      </div>
      <nav className="main-navigation" aria-label="Navegação principal">
        <a href="#dashboard">
          <span aria-hidden="true">⌂</span>
          Dashboard
        </a>

        <a href="#new-analysis" className="active" aria-current="page">
          <span aria-hidden="true">＋</span>
          Nova análise
        </a>

        <a href="#projects">
          <span aria-hidden="true">□</span>
          Projetos
        </a>

        <a href="#reports">
          <span aria-hidden="true">▤</span>
          Relatórios
        </a>

        <a href="#history">
          <span aria-hidden="true">◷</span>
          Histórico
        </a>

        <a href="#comparisons">
          <span aria-hidden="true">⇄</span>
          Comparações
        </a>

        <a href="#settings">
          <span aria-hidden="true">⚙</span>
          Configurações
        </a>
      </nav>

      <div className="sidebar-footer">
        <p>Desenvolvido com foco em acessibilidade digital.</p>
      </div>
    </aside>
  );
}

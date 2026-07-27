export function Header() {
  return (
    <header className="topbar">
      <div>
        <p className="eyebrow">Hub de Acessibilidade Digital</p>

        <h1>Nova análise</h1>

        <p className="page-description">
          Analise seu código HTML para identificar problemas de acessibilidade.
        </p>
      </div>

      <div className="user-area">
        <button
          className="notification-button"
          type="button"
          aria-label="Abrir notificações"
        >
          ♢
        </button>

        <div className="user-information">
          <div className="user-avatar" aria-hidden="true">
            G
          </div>

          <div>
            <strong>Gleica Dev</strong>
            <span>Plano gratuito</span>
          </div>
        </div>
      </div>
    </header>
  );
}

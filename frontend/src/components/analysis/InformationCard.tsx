export function InformationCard() {
  return (
    <aside className="information-card" aria-labelledby="information-title">
      <div>
        <p className="eyebrow">Verificações disponíveis</p>

        <h2 id="information-title">Sobre a análise</h2>

        <p>
          Nossa ferramenta verifica automaticamente aspectos estruturais de
          acessibilidade.
        </p>
      </div>

      <ul className="verification-list">
        <li>
          <span aria-hidden="true">✓</span>
          Estrutura semântica
        </li>

        <li>
          <span aria-hidden="true">✓</span>
          Imagens e textos alternativos
        </li>

        <li>
          <span aria-hidden="true">✓</span>
          Hierarquia de títulos
        </li>

        <li>
          <span aria-hidden="true">✓</span>
          Formulários e rótulos
        </li>

        <li>
          <span aria-hidden="true">✓</span>
          Links e botões
        </li>

        <li>
          <span aria-hidden="true">✓</span>
          Referências ARIA
        </li>
      </ul>

      <div className="notice-card">
        <strong>Importante</strong>
        <p>A análise automática não substitui uma auditoria manual completa.</p>
      </div>
    </aside>
  );
}

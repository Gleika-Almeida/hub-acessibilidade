import type { AppPage } from "./Sidebar";

interface HeaderProps {
  activePage: AppPage;
}

const pageContent: Record<
  AppPage,
  {
    title: string;
    description: string;
  }
> = {
  analysis: {
    title: "Nova análise",
    description:
      "Analise seu código HTML para identificar problemas de acessibilidade.",
  },
  history: {
    title: "Histórico",
    description:
      "Consulte as análises armazenadas e acompanhe os resultados anteriores.",
  },
};

export function Header({ activePage }: HeaderProps) {
  const content = pageContent[activePage];

  return (
    <header className="topbar">
      <div>
        <p className="eyebrow">Hub de Acessibilidade Digital</p>

        <h1>{content.title}</h1>

        <p className="page-description">{content.description}</p>
      </div>

      <div className="user-area">
        <div className="user-information">
          <div className="user-avatar" aria-hidden="true">
            G
          </div>

          <div>
            <strong>Gleica Dev</strong>
            <span>Ambiente local</span>
          </div>
        </div>
      </div>
    </header>
  );
}

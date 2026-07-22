import { useState } from "react";
import type { FormEvent } from "react";
import "./App.css";

type AnalysisTab = "url" | "html" | "css";

const initialHtml = `<!DOCTYPE html>
<html lang="pt-BR">
  <head>
    <title>Minha página</title>
  </head>

  <body>
    <h1>Produtos</h1>

    <img src="produto.jpg">

    <button></button>
  </body>
</html>`;

function App() {
  const [activeTab, setActiveTab] =
    useState<AnalysisTab>("html");

  const [htmlCode, setHtmlCode] =
    useState(initialHtml);

  const [url, setUrl] = useState("");

  const [cssCode, setCssCode] = useState("");

  const [wcagVersion, setWcagVersion] =
    useState("WCAG 2.2");

  const [conformanceLevel, setConformanceLevel] =
    useState("AA");

  const [includeContrast, setIncludeContrast] =
    useState(true);

  const [statusMessage, setStatusMessage] =
    useState("");

  function handleSubmit(
    event: FormEvent<HTMLFormElement>
  ) {
    event.preventDefault();

    setStatusMessage(
      "A análise será conectada ao backend na próxima etapa."
    );
  }

  return (
    <div className="app-shell">
      <a className="skip-link" href="#main-content">
        Pular para o conteúdo principal
      </a>

      <aside className="sidebar">
        <div className="brand">
          <div
            className="brand-icon"
            aria-hidden="true"
          >
            A
          </div>

          <div>
            <strong>A11Y Hub</strong>
            <span>Validador WCAG</span>
          </div>
        </div>

        <nav
          className="main-navigation"
          aria-label="Navegação principal"
        >
          <a href="#dashboard">
            <span aria-hidden="true">⌂</span>
            Dashboard
          </a>

          <a
            href="#new-analysis"
            className="active"
            aria-current="page"
          >
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
          <p>
            Desenvolvido com foco em acessibilidade
            digital.
          </p>
        </div>
      </aside>

      <div className="page-container">
        <header className="topbar">
          <div>
            <p className="eyebrow">
              Hub de Acessibilidade Digital
            </p>

            <h1>Nova análise</h1>

            <p className="page-description">
              Analise uma URL ou cole seu código para
              identificar problemas de acessibilidade.
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
              <div
                className="user-avatar"
                aria-hidden="true"
              >
                G
              </div>

              <div>
                <strong>Gleica Dev</strong>
                <span>Plano gratuito</span>
              </div>
            </div>
          </div>
        </header>

        <main id="main-content" className="main-content">
          <section
            className="analysis-section"
            id="new-analysis"
            aria-labelledby="analysis-title"
          >
            <div className="analysis-card">
              <div className="card-heading">
                <div>
                  <p className="eyebrow">
                    Auditoria automatizada
                  </p>

                  <h2 id="analysis-title">
                    O que deseja analisar?
                  </h2>
                </div>

                <span className="status-badge">
                  WCAG 2.2
                </span>
              </div>

              <div
                className="tabs"
                role="tablist"
                aria-label="Tipo de análise"
              >
                <button
                  id="tab-url"
                  className={
                    activeTab === "url"
                      ? "tab active"
                      : "tab"
                  }
                  type="button"
                  role="tab"
                  aria-selected={activeTab === "url"}
                  aria-controls="panel-url"
                  onClick={() => setActiveTab("url")}
                >
                  Analisar URL
                </button>

                <button
                  id="tab-html"
                  className={
                    activeTab === "html"
                      ? "tab active"
                      : "tab"
                  }
                  type="button"
                  role="tab"
                  aria-selected={activeTab === "html"}
                  aria-controls="panel-html"
                  onClick={() => setActiveTab("html")}
                >
                  Colar HTML
                </button>

                <button
                  id="tab-css"
                  className={
                    activeTab === "css"
                      ? "tab active"
                      : "tab"
                  }
                  type="button"
                  role="tab"
                  aria-selected={activeTab === "css"}
                  aria-controls="panel-css"
                  onClick={() => setActiveTab("css")}
                >
                  Colar CSS
                </button>
              </div>

              <form onSubmit={handleSubmit}>
                {activeTab === "url" && (
                  <div
                    id="panel-url"
                    role="tabpanel"
                    aria-labelledby="tab-url"
                    className="tab-panel"
                  >
                    <label htmlFor="website-url">
                      URL do site
                    </label>

                    <input
                      id="website-url"
                      type="url"
                      value={url}
                      onChange={(event) =>
                        setUrl(event.target.value)
                      }
                      placeholder="https://exemplo.com"
                      required
                    />

                    <p className="field-help">
                      Informe uma URL pública iniciada por
                      http ou https.
                    </p>
                  </div>
                )}

                {activeTab === "html" && (
                  <div
                    id="panel-html"
                    role="tabpanel"
                    aria-labelledby="tab-html"
                    className="tab-panel"
                  >
                    <label htmlFor="html-code">
                      Código HTML
                    </label>

                    <textarea
                      id="html-code"
                      value={htmlCode}
                      onChange={(event) =>
                        setHtmlCode(event.target.value)
                      }
                      rows={14}
                      spellCheck={false}
                      required
                    />

                    <p className="field-help">
                      Cole um documento HTML completo ou um
                      fragmento da interface.
                    </p>
                  </div>
                )}

                {activeTab === "css" && (
                  <div
                    id="panel-css"
                    role="tabpanel"
                    aria-labelledby="tab-css"
                    className="tab-panel"
                  >
                    <label htmlFor="css-code">
                      Código CSS
                    </label>

                    <textarea
                      id="css-code"
                      value={cssCode}
                      onChange={(event) =>
                        setCssCode(event.target.value)
                      }
                      rows={14}
                      spellCheck={false}
                      placeholder={`button:focus {
  outline: none;
}`}
                      required
                    />

                    <p className="field-help">
                      Esta funcionalidade será implementada
                      em uma etapa futura.
                    </p>
                  </div>
                )}

                <fieldset className="settings-fieldset">
                  <legend>
                    Configurações da análise
                  </legend>

                  <div className="settings-grid">
                    <div className="form-field">
                      <label htmlFor="wcag-version">
                        Padrão WCAG
                      </label>

                      <select
                        id="wcag-version"
                        value={wcagVersion}
                        onChange={(event) =>
                          setWcagVersion(
                            event.target.value
                          )
                        }
                      >
                        <option>WCAG 2.2</option>
                        <option>WCAG 2.1</option>
                        <option>WCAG 2.0</option>
                      </select>
                    </div>

                    <div className="form-field">
                      <label htmlFor="conformance-level">
                        Nível de conformidade
                      </label>

                      <select
                        id="conformance-level"
                        value={conformanceLevel}
                        onChange={(event) =>
                          setConformanceLevel(
                            event.target.value
                          )
                        }
                      >
                        <option>A</option>
                        <option>AA</option>
                        <option>AAA</option>
                      </select>
                    </div>
                  </div>

                  <label className="switch-field">
                    <span>
                      <strong>
                        Incluir análise de contraste
                      </strong>

                      <small>
                        Verificar cores de textos e
                        elementos visuais.
                      </small>
                    </span>

                    <input
                      type="checkbox"
                      checked={includeContrast}
                      onChange={(event) =>
                        setIncludeContrast(
                          event.target.checked
                        )
                      }
                    />
                  </label>
                </fieldset>

                <button
                  className="primary-button"
                  type="submit"
                >
                  Iniciar análise
                  <span aria-hidden="true">→</span>
                </button>

                <div
                  className="status-region"
                  role="status"
                  aria-live="polite"
                >
                  {statusMessage}
                </div>
              </form>
            </div>

            <aside
              className="information-card"
              aria-labelledby="information-title"
            >
              <div>
                <p className="eyebrow">
                  Verificações disponíveis
                </p>

                <h2 id="information-title">
                  Sobre a análise
                </h2>

                <p>
                  Nossa ferramenta verifica
                  automaticamente aspectos estruturais de
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

                <p>
                  A análise automática não substitui uma
                  auditoria manual completa.
                </p>
              </div>
            </aside>
          </section>
        </main>
      </div>
    </div>
  );
}

export default App;
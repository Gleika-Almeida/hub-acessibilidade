import { useState, type FormEvent } from "react";

type AnalysisTab = "url" | "html" | "css";

interface AnalysisFormProps {
  htmlCode: string;
  isLoading: boolean;
  statusMessage: string;
  errorMessage: string;
  onHtmlChange: (html: string) => void;
  onAnalyze: () => Promise<void>;
}

export function AnalysisForm({
  htmlCode,
  isLoading,
  statusMessage,
  errorMessage,
  onHtmlChange,
  onAnalyze,
}: AnalysisFormProps) {
  const [activeTab, setActiveTab] = useState<AnalysisTab>("html");

  const [url, setUrl] = useState("");
  const [cssCode, setCssCode] = useState("");

  const [wcagVersion, setWcagVersion] = useState("WCAG 2.2");

  const [conformanceLevel, setConformanceLevel] = useState("AA");

  const [includeContrast, setIncludeContrast] = useState(true);

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    if (activeTab !== "html") {
      return;
    }

    await onAnalyze();
  }

  return (
    <div className="analysis-card">
      <div className="card-heading">
        <div>
          <p className="eyebrow">Auditoria automatizada</p>

          <h2 id="analysis-title">O que deseja analisar?</h2>
        </div>

        <span className="status-badge">WCAG 2.2</span>
      </div>

      <div className="tabs" role="tablist" aria-label="Tipo de análise">
        <button
          id="tab-url"
          className={activeTab === "url" ? "tab active" : "tab"}
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
          className={activeTab === "html" ? "tab active" : "tab"}
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
          className={activeTab === "css" ? "tab active" : "tab"}
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
            <label htmlFor="website-url">URL do site</label>

            <input
              id="website-url"
              type="url"
              value={url}
              onChange={(event) => setUrl(event.target.value)}
              placeholder="https://exemplo.com"
            />

            <p className="field-help">
              A análise por URL será adicionada posteriormente.
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
            <label htmlFor="html-code">Código HTML</label>

            <textarea
              id="html-code"
              value={htmlCode}
              onChange={(event) => onHtmlChange(event.target.value)}
              rows={14}
              spellCheck={false}
              required
            />

            <p className="field-help">
              Cole um documento HTML completo ou um fragmento da interface.
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
            <label htmlFor="css-code">Código CSS</label>

            <textarea
              id="css-code"
              value={cssCode}
              onChange={(event) => setCssCode(event.target.value)}
              rows={14}
              spellCheck={false}
              placeholder={`button:focus {
  outline: none;
}`}
            />

            <p className="field-help">
              A análise de CSS será implementada em uma etapa futura.
            </p>
          </div>
        )}

        <fieldset className="settings-fieldset">
          <legend>Configurações da análise</legend>

          <div className="settings-grid">
            <div className="form-field">
              <label htmlFor="wcag-version">Padrão WCAG</label>

              <select
                id="wcag-version"
                value={wcagVersion}
                onChange={(event) => setWcagVersion(event.target.value)}
              >
                <option>WCAG 2.2</option>
                <option>WCAG 2.1</option>
                <option>WCAG 2.0</option>
              </select>
            </div>

            <div className="form-field">
              <label htmlFor="conformance-level">Nível de conformidade</label>

              <select
                id="conformance-level"
                value={conformanceLevel}
                onChange={(event) => setConformanceLevel(event.target.value)}
              >
                <option>A</option>
                <option>AA</option>
                <option>AAA</option>
              </select>
            </div>
          </div>

          <label className="switch-field">
            <span>
              <strong>Incluir análise de contraste</strong>

              <small>Esta verificação será implementada posteriormente.</small>
            </span>

            <input
              type="checkbox"
              checked={includeContrast}
              onChange={(event) => setIncludeContrast(event.target.checked)}
            />
          </label>
        </fieldset>

        <button
          className="primary-button"
          type="submit"
          disabled={isLoading || activeTab !== "html"}
        >
          {isLoading ? "Analisando..." : "Iniciar análise"}

          {!isLoading && <span aria-hidden="true">→</span>}
        </button>

        <div className="status-region" role="status" aria-live="polite">
          {isLoading && <span>O código está sendo analisado.</span>}

          {!isLoading && statusMessage && <span>{statusMessage}</span>}
        </div>

        {errorMessage && (
          <div className="error-message" role="alert">
            <strong>Não foi possível analisar.</strong>

            <p>{errorMessage}</p>
          </div>
        )}
      </form>
    </div>
  );
}

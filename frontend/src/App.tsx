import { useState } from "react";
import { AnalysisForm } from "./components/analysis/AnalysisForm";
import { InformationCard } from "./components/analysis/InformationCard";
import { Header } from "./components/layout/Header";
import { Sidebar } from "./components/layout/Sidebar";
import { ScanReportView } from "./components/report/ScanReportView";
import { scanHtml } from "./services/scanApi";
import type { ScanReport } from "./types";
import "./App.css";

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
  const [htmlCode, setHtmlCode] = useState(initialHtml);

  const [report, setReport] = useState<ScanReport | null>(null);

  const [isLoading, setIsLoading] = useState(false);

  const [statusMessage, setStatusMessage] = useState("");

  const [errorMessage, setErrorMessage] = useState("");

  async function handleAnalyze() {
    setStatusMessage("");
    setErrorMessage("");
    setReport(null);

    if (!htmlCode.trim()) {
      setErrorMessage("Cole um código HTML antes de iniciar a análise.");

      return;
    }

    try {
      setIsLoading(true);

      const result = await scanHtml(htmlCode);

      setReport(result);

      setStatusMessage("Análise concluída com sucesso.");
    } catch (error) {
      const message =
        error instanceof Error ? error.message : "Ocorreu um erro inesperado.";

      setErrorMessage(message);
    } finally {
      setIsLoading(false);
    }
  }

  return (
    <div className="app-shell">
      <a className="skip-link" href="#main-content">
        Pular para o conteúdo principal
      </a>

      <Sidebar />

      <div className="page-container">
        <Header />

        <main id="main-content" className="main-content">
          <section
            className="analysis-section"
            id="new-analysis"
            aria-labelledby="analysis-title"
          >
            <AnalysisForm
              htmlCode={htmlCode}
              isLoading={isLoading}
              statusMessage={statusMessage}
              errorMessage={errorMessage}
              onHtmlChange={setHtmlCode}
              onAnalyze={handleAnalyze}
            />

            <InformationCard />
          </section>

          {report && <ScanReportView report={report} />}
        </main>
      </div>
    </div>
  );
}

export default App;

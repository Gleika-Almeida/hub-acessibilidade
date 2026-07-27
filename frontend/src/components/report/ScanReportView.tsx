import type { ScanReport } from "../../types";
import { IssueCard } from "./IssueCard";
import { SummaryCard } from "./SummaryCard";

interface ScanReportViewProps {
  report: ScanReport;
}

export function ScanReportView({ report }: ScanReportViewProps) {
  const analyzedAt = new Intl.DateTimeFormat("pt-BR", {
    dateStyle: "short",
    timeStyle: "short",
  }).format(new Date(report.analyzedAt));

  return (
    <section className="report-section" aria-labelledby="report-title">
      <div className="report-heading">
        <div>
          <p className="eyebrow">Resultado automatizado</p>

          <h2 id="report-title">Relatório de acessibilidade</h2>

          <p>Análise concluída em {analyzedAt}</p>
        </div>

        <div
          className="score-card"
          aria-label={`Pontuação ${report.score} de 100`}
        >
          <strong>{report.score}</strong>
          <span>de 100</span>
        </div>
      </div>

      <div className="score-progress">
        <label htmlFor="score">Pontuação da auditoria</label>

        <progress id="score" max="100" value={report.score}>
          {report.score}%
        </progress>
      </div>

      <p className="automatic-review-notice">
        Esta pontuação representa somente as verificações automatizadas
        disponíveis e não substitui uma auditoria manual.
      </p>

      <div className="summary-grid">
        <SummaryCard label="Total" value={report.summary.total} />

        <SummaryCard label="Críticos" value={report.summary.critical} />

        <SummaryCard label="Graves" value={report.summary.serious} />

        <SummaryCard label="Moderados" value={report.summary.moderate} />

        <SummaryCard label="Leves" value={report.summary.minor} />
      </div>

      <div className="issues-heading">
        <div>
          <h3>Problemas encontrados</h3>

          <p>Analise cada ocorrência e aplique as recomendações sugeridas.</p>
        </div>

        <span className="issues-counter">
          {report.summary.total} ocorrência(s)
        </span>
      </div>

      {report.issues.length === 0 ? (
        <div className="success-message" role="status">
          <strong>Nenhum problema automático encontrado</strong>

          <p>O código passou pelas verificações disponíveis nesta versão.</p>
        </div>
      ) : (
        <div className="issues-list">
          {report.issues.map((issue, index) => (
            <IssueCard key={`${issue.ruleId}-${index}`} issue={issue} />
          ))}
        </div>
      )}
    </section>
  );
}

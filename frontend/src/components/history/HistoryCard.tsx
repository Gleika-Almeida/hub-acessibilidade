import type { ScanHistoryItem } from "../../types";

interface HistoryCardProps {
  scan: ScanHistoryItem;
  isDeleting: boolean;
  onViewDetails: (id: number) => void;
  onDelete: (id: number) => void;
}

function formatDate(date: string): string {
  return new Intl.DateTimeFormat("pt-BR", {
    dateStyle: "short",
    timeStyle: "short",
  }).format(new Date(date));
}

export function HistoryCard({
  scan,
  isDeleting,
  onViewDetails,
  onDelete,
}: HistoryCardProps) {
  return (
    <article className="history-card">
      <div className="history-card-header">
        <div>
          <span className="history-id">Análise #{scan.id}</span>

          <h3>Código {scan.sourceType}</h3>
        </div>

        <div
          className="history-score"
          aria-label={`Pontuação ${scan.score} de 100`}
        >
          {scan.score}
        </div>
      </div>

      <dl className="history-data">
        <div>
          <dt>Data da análise</dt>
          <dd>{formatDate(scan.analyzedAt)}</dd>
        </div>

        <div>
          <dt>Total de problemas</dt>
          <dd>{scan.summary.total}</dd>
        </div>

        <div>
          <dt>Críticos</dt>
          <dd>{scan.summary.critical}</dd>
        </div>

        <div>
          <dt>Graves</dt>
          <dd>{scan.summary.serious}</dd>
        </div>
      </dl>

      <div className="history-card-actions">
        <button
          className="secondary-button"
          type="button"
          onClick={() => onViewDetails(scan.id)}
        >
          Ver detalhes
        </button>

        <button
          className="danger-button"
          type="button"
          disabled={isDeleting}
          onClick={() => onDelete(scan.id)}
        >
          {isDeleting ? "Excluindo..." : "Excluir"}
        </button>
      </div>
    </article>
  );
}

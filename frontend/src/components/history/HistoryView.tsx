import { useEffect, useState } from "react";
import {
  deleteScan,
  getScanById,
  getScanHistory,
} from "../../services/historyApi";
import type { ScanHistoryDetail, ScanHistoryItem } from "../../types";
import { ScanReportView } from "../report/ScanReportView";
import { HistoryCard } from "./HistoryCard";

export function HistoryView() {
  const [history, setHistory] = useState<ScanHistoryItem[]>([]);

  const [selectedScan, setSelectedScan] = useState<ScanHistoryDetail | null>(
    null,
  );

  const [isLoading, setIsLoading] = useState(true);

  const [isLoadingDetails, setIsLoadingDetails] = useState(false);

  const [deletingId, setDeletingId] = useState<number | null>(null);

  const [errorMessage, setErrorMessage] = useState("");

  const [statusMessage, setStatusMessage] = useState("");

  useEffect(() => {
    let isMounted = true;

    async function loadInitialHistory() {
      try {
        const result = await getScanHistory();

        if (isMounted) {
          setHistory(result);
        }
      } catch (error) {
        if (isMounted) {
          setErrorMessage(
            error instanceof Error
              ? error.message
              : "Ocorreu um erro inesperado.",
          );
        }
      } finally {
        if (isMounted) {
          setIsLoading(false);
        }
      }
    }

    void loadInitialHistory();

    return () => {
      isMounted = false;
    };
  }, []);

  async function handleRefreshHistory() {
    try {
      setIsLoading(true);
      setErrorMessage("");
      setStatusMessage("");

      const result = await getScanHistory();

      setHistory(result);
      setStatusMessage("Histórico atualizado com sucesso.");
    } catch (error) {
      setErrorMessage(
        error instanceof Error ? error.message : "Ocorreu um erro inesperado.",
      );
    } finally {
      setIsLoading(false);
    }
  }

  async function handleViewDetails(id: number) {
    try {
      setIsLoadingDetails(true);
      setErrorMessage("");
      setStatusMessage("");

      const result = await getScanById(id);

      setSelectedScan(result);

      window.setTimeout(() => {
        document.getElementById("history-details")?.scrollIntoView({
          behavior: "smooth",
          block: "start",
        });
      }, 0);
    } catch (error) {
      setErrorMessage(
        error instanceof Error
          ? error.message
          : "Não foi possível carregar os detalhes.",
      );
    } finally {
      setIsLoadingDetails(false);
    }
  }

  async function handleDelete(id: number) {
    const confirmed = window.confirm(
      `Deseja excluir a análise #${id}? Essa ação não poderá ser desfeita.`,
    );

    if (!confirmed) {
      return;
    }

    try {
      setDeletingId(id);
      setErrorMessage("");
      setStatusMessage("");

      await deleteScan(id);

      setHistory((currentHistory) =>
        currentHistory.filter((scan) => scan.id !== id),
      );

      if (selectedScan?.id === id) {
        setSelectedScan(null);
      }

      setStatusMessage(`A análise #${id} foi excluída com sucesso.`);
    } catch (error) {
      setErrorMessage(
        error instanceof Error
          ? error.message
          : "Não foi possível excluir a análise.",
      );
    } finally {
      setDeletingId(null);
    }
  }

  return (
    <section className="history-section" aria-labelledby="history-title">
      <div className="history-heading">
        <div>
          <p className="eyebrow">Análises armazenadas</p>

          <h2 id="history-title">Histórico</h2>

          <p>
            Consulte os resultados anteriores e acompanhe a evolução da
            acessibilidade.
          </p>
        </div>

        <button
          className="secondary-button"
          type="button"
          disabled={isLoading}
          onClick={() => void handleRefreshHistory()}
        >
          Atualizar histórico
        </button>
      </div>

      <div className="status-region" role="status" aria-live="polite">
        {statusMessage}
      </div>

      {errorMessage && (
        <div className="error-message" role="alert">
          <strong>Não foi possível concluir a operação.</strong>

          <p>{errorMessage}</p>
        </div>
      )}

      {isLoading && (
        <div className="history-loading" role="status">
          Carregando histórico...
        </div>
      )}

      {!isLoading && history.length === 0 && (
        <div className="history-empty">
          <h3>Nenhuma análise encontrada</h3>

          <p>
            Realize uma análise de HTML para que ela apareça neste histórico.
          </p>
        </div>
      )}

      {!isLoading && history.length > 0 && (
        <>
          <p className="history-count">
            {history.length} análise(s) encontrada(s)
          </p>

          <div className="history-grid">
            {history.map((scan) => (
              <HistoryCard
                key={scan.id}
                scan={scan}
                isDeleting={deletingId === scan.id}
                onViewDetails={handleViewDetails}
                onDelete={handleDelete}
              />
            ))}
          </div>
        </>
      )}

      {isLoadingDetails && (
        <div className="history-loading" role="status">
          Carregando detalhes da análise...
        </div>
      )}

      {selectedScan && !isLoadingDetails && (
        <div id="history-details">
          <div className="history-detail-actions">
            <h2>Detalhes da análise #{selectedScan.id}</h2>

            <button
              className="secondary-button"
              type="button"
              onClick={() => setSelectedScan(null)}
            >
              Fechar detalhes
            </button>
          </div>

          {selectedScan.sourceValue && (
            <details className="source-code-details">
              <summary>Ver código analisado</summary>

              <pre>
                <code>{selectedScan.sourceValue}</code>
              </pre>
            </details>
          )}

          <ScanReportView report={selectedScan} />
        </div>
      )}
    </section>
  );
}

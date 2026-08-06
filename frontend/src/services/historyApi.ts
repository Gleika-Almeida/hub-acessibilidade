import type { ScanHistoryDetail, ScanHistoryItem } from "../types";

const API_URL = import.meta.env.VITE_API_URL ?? "http://localhost:8080";

async function handleResponseError(response: Response): Promise<never> {
  if (response.status === 404) {
    throw new Error("A análise selecionada não foi encontrada.");
  }

  throw new Error(
    "Não foi possível acessar o histórico. Verifique se o backend está funcionando.",
  );
}

export async function getScanHistory(): Promise<ScanHistoryItem[]> {
  const response = await fetch(`${API_URL}/api/scans`);

  if (!response.ok) {
    return handleResponseError(response);
  }

  return response.json() as Promise<ScanHistoryItem[]>;
}

export async function getScanById(id: number): Promise<ScanHistoryDetail> {
  const response = await fetch(`${API_URL}/api/scans/${id}`);

  if (!response.ok) {
    return handleResponseError(response);
  }

  return response.json() as Promise<ScanHistoryDetail>;
}

export async function deleteScan(id: number): Promise<void> {
  const response = await fetch(`${API_URL}/api/scans/${id}`, {
    method: "DELETE",
  });

  if (!response.ok) {
    return handleResponseError(response);
  }
}

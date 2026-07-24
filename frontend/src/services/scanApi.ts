import type { ScanReport } from "../types";

const API_URL =
  import.meta.env.VITE_API_URL ??
  "http://localhost:8080";

export async function scanHtml(
  html: string
): Promise<ScanReport> {
  const response = await fetch(
    `${API_URL}/api/scans/html`,
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ html }),
    }
  );

  if (!response.ok) {
    if (response.status === 400) {
      throw new Error(
        "O código HTML está vazio ou possui dados inválidos."
      );
    }

    throw new Error(
      "Não foi possível concluir a análise. Verifique se o backend está funcionando."
    );
  }

  return response.json() as Promise<ScanReport>;
}
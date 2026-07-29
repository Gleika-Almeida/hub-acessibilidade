import { render, screen } from "@testing-library/react";
import { ScanReportView } from "./ScanReportView";
import type { ScanReport } from "../../types";

const reportWithIssue: ScanReport = {
  score: 80,
  summary: {
    total: 1,
    critical: 0,
    serious: 1,
    moderate: 0,
    minor: 0,
  },
  issues: [
    {
      ruleId: "image-alt",
      title: "Imagem sem atributo alt",
      description: "A imagem não possui uma alternativa textual.",
      severity: "SERIOUS",
      wcagCriterion: "1.1.1",
      selector: "img",
      snippet: '<img src="produto.jpg">',
      recommendation: "Adicione um atributo alt descritivo.",
    },
  ],
  analyzedAt: "2026-07-22T12:00:00Z",
};

const accessibleReport: ScanReport = {
  score: 100,
  summary: {
    total: 0,
    critical: 0,
    serious: 0,
    moderate: 0,
    minor: 0,
  },
  issues: [],
  analyzedAt: "2026-07-22T12:00:00Z",
};

describe("ScanReportView", () => {
  it("deve mostrar a pontuação e os problemas", () => {
    render(<ScanReportView report={reportWithIssue} />);

    expect(
      screen.getByRole("heading", {
        name: "Relatório de acessibilidade",
      }),
    ).toBeInTheDocument();

    expect(screen.getByLabelText("Pontuação 80 de 100")).toBeInTheDocument();

    expect(screen.getByText("Imagem sem atributo alt")).toBeInTheDocument();
  });

  it("deve exibir mensagem de sucesso quando não houver problemas", () => {
    render(<ScanReportView report={accessibleReport} />);

    expect(
      screen.getByText("Nenhum problema automático encontrado"),
    ).toBeInTheDocument();
  });
});

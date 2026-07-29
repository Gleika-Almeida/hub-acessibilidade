import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { vi } from "vitest";
import { AnalysisForm } from "./AnalysisForm";

describe("AnalysisForm", () => {
  it("deve permitir alterar o código HTML", async () => {
    const user = userEvent.setup();
    const handleHtmlChange = vi.fn();

    render(
      <AnalysisForm
        htmlCode="<h1>Teste</h1>"
        isLoading={false}
        statusMessage=""
        errorMessage=""
        onHtmlChange={handleHtmlChange}
        onAnalyze={vi.fn()}
      />,
    );

    const textarea = screen.getByLabelText("Código HTML");

    await user.type(textarea, " novo");

    expect(handleHtmlChange).toHaveBeenCalled();
  });

  it("deve executar a análise ao enviar o formulário", async () => {
    const user = userEvent.setup();
    const handleAnalyze = vi.fn().mockResolvedValue(undefined);

    render(
      <AnalysisForm
        htmlCode="<h1>Teste</h1>"
        isLoading={false}
        statusMessage=""
        errorMessage=""
        onHtmlChange={vi.fn()}
        onAnalyze={handleAnalyze}
      />,
    );

    await user.click(
      screen.getByRole("button", {
        name: /iniciar análise/i,
      }),
    );

    expect(handleAnalyze).toHaveBeenCalledTimes(1);
  });

  it("deve apresentar o estado de carregamento", () => {
    render(
      <AnalysisForm
        htmlCode="<h1>Teste</h1>"
        isLoading
        statusMessage=""
        errorMessage=""
        onHtmlChange={vi.fn()}
        onAnalyze={vi.fn()}
      />,
    );

    expect(
      screen.getByRole("button", {
        name: "Analisando...",
      }),
    ).toBeDisabled();

    expect(
      screen.getByText("O código está sendo analisado."),
    ).toBeInTheDocument();
  });

  it("deve mostrar a mensagem de erro", () => {
    render(
      <AnalysisForm
        htmlCode=""
        isLoading={false}
        statusMessage=""
        errorMessage="Cole um código HTML."
        onHtmlChange={vi.fn()}
        onAnalyze={vi.fn()}
      />,
    );

    expect(screen.getByRole("alert")).toHaveTextContent("Cole um código HTML.");
  });
});

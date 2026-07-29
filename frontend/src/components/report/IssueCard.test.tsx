import { render, screen } from "@testing-library/react";
import { IssueCard } from "./IssueCard";
import type { AccessibilityIssue } from "../../types";

const issue: AccessibilityIssue = {
  ruleId: "image-alt",
  title: "Imagem sem atributo alt",
  description: "A imagem não possui uma alternativa textual.",
  severity: "SERIOUS",
  wcagCriterion: "1.1.1",
  selector: "img.produto",
  snippet: '<img class="produto" src="produto.jpg">',
  recommendation: "Adicione um atributo alt descritivo.",
};

describe("IssueCard", () => {
  it("deve apresentar os dados do problema", () => {
    render(<IssueCard issue={issue} />);

    expect(
      screen.getByRole("heading", {
        name: "Imagem sem atributo alt",
      }),
    ).toBeInTheDocument();

    expect(screen.getByText("Grave")).toBeInTheDocument();

    expect(screen.getByText("WCAG 1.1.1")).toBeInTheDocument();

    expect(screen.getByText("img.produto")).toBeInTheDocument();

    expect(
      screen.getByText("Adicione um atributo alt descritivo."),
    ).toBeInTheDocument();
  });
});

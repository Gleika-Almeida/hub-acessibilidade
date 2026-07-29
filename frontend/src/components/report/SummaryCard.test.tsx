import { render, screen } from "@testing-library/react";
import { SummaryCard } from "./SummaryCard";

describe("SummaryCard", () => {
  it("deve exibir o rótulo e o valor recebidos", () => {
    render(<SummaryCard label="Críticos" value={4} />);

    expect(screen.getByText("Críticos")).toBeInTheDocument();

    expect(screen.getByText("4")).toBeInTheDocument();
  });
});

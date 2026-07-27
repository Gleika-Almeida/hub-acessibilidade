import type { AccessibilityIssue, Severity } from "../../types";

interface IssueCardProps {
  issue: AccessibilityIssue;
}

const severityLabels: Record<Severity, string> = {
  CRITICAL: "Crítico",
  SERIOUS: "Grave",
  MODERATE: "Moderado",
  MINOR: "Leve",
};

export function IssueCard({ issue }: IssueCardProps) {
  return (
    <article className="issue-card">
      <div className="issue-card-heading">
        <div>
          <span className="rule-id">{issue.ruleId}</span>

          <h4>{issue.title}</h4>
        </div>

        <span className={`severity severity-${issue.severity.toLowerCase()}`}>
          {severityLabels[issue.severity]}
        </span>
      </div>

      <p>{issue.description}</p>

      <dl className="issue-details">
        <div>
          <dt>Critério relacionado</dt>
          <dd>WCAG {issue.wcagCriterion}</dd>
        </div>

        <div>
          <dt>Elemento</dt>
          <dd>
            <code>{issue.selector}</code>
          </dd>
        </div>
      </dl>

      {issue.snippet && (
        <div className="code-block">
          <span>Código encontrado</span>

          <pre>
            <code>{issue.snippet}</code>
          </pre>
        </div>
      )}

      <div className="recommendation">
        <strong>Como corrigir</strong>
        <p>{issue.recommendation}</p>
      </div>
    </article>
  );
}

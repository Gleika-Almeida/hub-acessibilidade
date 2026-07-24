export type Severity =
  | "CRITICAL"
  | "SERIOUS"
  | "MODERATE"
  | "MINOR";

export interface AccessibilityIssue {
  ruleId: string;
  title: string;
  description: string;
  severity: Severity;
  wcagCriterion: string;
  selector: string;
  snippet: string;
  recommendation: string;
}

export interface ScanSummary {
  total: number;
  critical: number;
  serious: number;
  moderate: number;
  minor: number;
}

export interface ScanReport {
  score: number;
  summary: ScanSummary;
  issues: AccessibilityIssue[];
  analyzedAt: string;
}
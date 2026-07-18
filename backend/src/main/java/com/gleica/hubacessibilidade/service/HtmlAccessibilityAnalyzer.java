package com.gleica.hubacessibilidade.service;

import com.gleica.hubacessibilidade.dto.AccessibilityIssue;
import com.gleica.hubacessibilidade.dto.ScanReport;
import com.gleica.hubacessibilidade.dto.ScanSummary;
import com.gleica.hubacessibilidade.model.Severity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class HtmlAccessibilityAnalyzer {

    public ScanReport analyze(String html) {
        Document document = Jsoup.parse(html);

        List<AccessibilityIssue> issues = new ArrayList<>();

        checkDocumentLanguage(document, issues);
        checkDocumentTitle(document, issues);
        checkHeadingOne(document, issues);
        checkImages(document, issues);

        ScanSummary summary = createSummary(issues);
        int score = calculateScore(issues);

        return new ScanReport(
                score,
                summary,
                List.copyOf(issues),
                Instant.now()
        );
    }

    private void checkDocumentLanguage(
            Document document,
            List<AccessibilityIssue> issues
    ) {
        Element htmlElement = document.selectFirst("html");

        boolean hasLanguage = htmlElement != null
                && !htmlElement.attr("lang").isBlank();

        if (!hasLanguage) {
            issues.add(
                    new AccessibilityIssue(
                            "html-has-lang",
                            "Idioma da página não informado",
                            "O elemento html não declara o idioma principal da página.",
                            Severity.SERIOUS,
                            "3.1.1",
                            "html",
                            createSnippet(htmlElement),
                            "Adicione o atributo lang ao elemento html, como <html lang=\"pt-BR\">."
                    )
            );
        }
    }

    private void checkDocumentTitle(
            Document document,
            List<AccessibilityIssue> issues
    ) {
        if (document.title().isBlank()) {
            Element head = document.head();

            issues.add(
                    new AccessibilityIssue(
                            "document-title",
                            "Página sem título",
                            "O documento não possui um título descritivo.",
                            Severity.SERIOUS,
                            "2.4.2",
                            "head > title",
                            createSnippet(head),
                            "Adicione um elemento <title> claro e específico dentro do <head>."
                    )
            );
        }
    }

    private void checkHeadingOne(
            Document document,
            List<AccessibilityIssue> issues
    ) {
        Element headingOne = document.selectFirst("h1");

        if (headingOne == null) {
            issues.add(
                    new AccessibilityIssue(
                            "page-has-heading-one",
                            "Página sem título principal",
                            "Nenhum elemento h1 foi encontrado na página.",
                            Severity.MODERATE,
                            "1.3.1",
                            "body",
                            createSnippet(document.body()),
                            "Inclua um h1 que descreva claramente o conteúdo principal da página."
                    )
            );
        }
    }

    private void checkImages(
            Document document,
            List<AccessibilityIssue> issues
    ) {
        for (Element image : document.select("img")) {
            if (!image.hasAttr("alt")) {
                issues.add(
                        new AccessibilityIssue(
                                "image-alt",
                                "Imagem sem atributo alt",
                                "A imagem não possui uma alternativa textual.",
                                Severity.SERIOUS,
                                "1.1.1",
                                createSelector(image),
                                createSnippet(image),
                                "Adicione um atributo alt descritivo ou use alt=\"\" quando a imagem for apenas decorativa."
                        )
                );
            }
        }
    }

    private ScanSummary createSummary(
            List<AccessibilityIssue> issues
    ) {
        int critical = countBySeverity(
                issues,
                Severity.CRITICAL
        );

        int serious = countBySeverity(
                issues,
                Severity.SERIOUS
        );

        int moderate = countBySeverity(
                issues,
                Severity.MODERATE
        );

        int minor = countBySeverity(
                issues,
                Severity.MINOR
        );

        return new ScanSummary(
                issues.size(),
                critical,
                serious,
                moderate,
                minor
        );
    }

    private int countBySeverity(
            List<AccessibilityIssue> issues,
            Severity severity
    ) {
        return (int) issues.stream()
                .filter(issue ->
                        issue.severity() == severity
                )
                .count();
    }

    private int calculateScore(
            List<AccessibilityIssue> issues
    ) {
        int score = 100;

        for (AccessibilityIssue issue : issues) {
            score -= severityPenalty(issue.severity());
        }

        return Math.max(score, 0);
    }

    private int severityPenalty(Severity severity) {
        return switch (severity) {
            case CRITICAL -> 20;
            case SERIOUS -> 10;
            case MODERATE -> 5;
            case MINOR -> 2;
        };
    }

    private String createSelector(Element element) {
        if (element == null) {
            return "document";
        }

        if (!element.id().isBlank()) {
            return element.tagName()
                    + "#"
                    + element.id();
        }

        if (!element.classNames().isEmpty()) {
            return element.tagName()
                    + "."
                    + String.join(
                            ".",
                            element.classNames()
                    );
        }

        return element.tagName();
    }

    private String createSnippet(Element element) {
        if (element == null) {
            return "";
        }

        String html = element
                .outerHtml()
                .replaceAll("\\s+", " ")
                .trim();

        if (html.length() <= 180) {
            return html;
        }

        return html.substring(0, 180) + "...";
    }
}
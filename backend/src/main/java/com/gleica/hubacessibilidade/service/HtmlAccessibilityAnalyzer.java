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
        checkHeadings(document, issues);
        checkImages(document, issues);
        checkFormLabels(document, issues);
        checkButtons(document, issues);
        checkLinks(document, issues);
        checkAriaReferences(document, issues);

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
            addIssue(
                    issues,
                    "html-has-lang",
                    "Idioma da página não informado",
                    "O elemento html não declara o idioma principal da página.",
                    Severity.SERIOUS,
                    "3.1.1",
                    htmlElement,
                    "Adicione o atributo lang ao elemento html, como <html lang=\"pt-BR\">."
            );
        }
    }

    private void checkDocumentTitle(
            Document document,
            List<AccessibilityIssue> issues
    ) {
        if (document.title().isBlank()) {
            addIssue(
                    issues,
                    "document-title",
                    "Página sem título",
                    "O documento não possui um título descritivo.",
                    Severity.SERIOUS,
                    "2.4.2",
                    document.head(),
                    "Adicione um elemento <title> claro e específico dentro do <head>."
            );
        }
    }

    private void checkHeadings(
            Document document,
            List<AccessibilityIssue> issues
    ) {
        List<Element> headings = document
                .select("h1, h2, h3, h4, h5, h6")
                .stream()
                .toList();

        boolean hasHeadingOne = headings.stream()
                .anyMatch(heading -> heading.tagName().equals("h1"));

        if (!hasHeadingOne) {
            addIssue(
                    issues,
                    "page-has-heading-one",
                    "Página sem título principal",
                    "Nenhum elemento h1 foi encontrado na página.",
                    Severity.MODERATE,
                    "1.3.1",
                    document.body(),
                    "Inclua um h1 que descreva claramente o conteúdo principal da página."
            );
        }

        int previousLevel = 0;

        for (Element heading : headings) {
            int currentLevel = Integer.parseInt(
                    heading.tagName().substring(1)
            );

            if (heading.text().isBlank()) {
                addIssue(
                        issues,
                        "empty-heading",
                        "Título vazio",
                        "Um elemento de título foi criado sem conteúdo textual.",
                        Severity.SERIOUS,
                        "1.3.1",
                        heading,
                        "Adicione um texto descritivo ou remova o título vazio."
                );
            }

            if (
                    previousLevel > 0
                            && currentLevel > previousLevel + 1
            ) {
                addIssue(
                        issues,
                        "heading-order",
                        "Nível de título ignorado",
                        "A hierarquia passou de h"
                                + previousLevel
                                + " diretamente para h"
                                + currentLevel
                                + ".",
                        Severity.MODERATE,
                        "1.3.1",
                        heading,
                        "Organize os títulos sem pular níveis hierárquicos."
                );
            }

            previousLevel = currentLevel;
        }
    }

    private void checkImages(
            Document document,
            List<AccessibilityIssue> issues
    ) {
        for (Element image : document.select("img")) {
            if (!image.hasAttr("alt")) {
                addIssue(
                        issues,
                        "image-alt",
                        "Imagem sem atributo alt",
                        "A imagem não possui uma alternativa textual.",
                        Severity.SERIOUS,
                        "1.1.1",
                        image,
                        "Adicione um atributo alt descritivo ou use alt=\"\" quando a imagem for apenas decorativa."
                );
            }
        }
    }

    private void checkFormLabels(
            Document document,
            List<AccessibilityIssue> issues
    ) {
        String selector =
                "input:not([type=hidden])"
                        + ":not([type=button])"
                        + ":not([type=submit])"
                        + ":not([type=reset]), "
                        + "select, textarea";

        for (Element control : document.select(selector)) {
            if (!hasFormLabel(control, document)) {
                addIssue(
                        issues,
                        "form-label",
                        "Campo sem rótulo acessível",
                        "O campo de formulário não possui label ou nome acessível.",
                        Severity.SERIOUS,
                        "1.3.1 e 3.3.2",
                        control,
                        "Associe um elemento <label> ao campo usando os atributos for e id."
                );
            }
        }
    }

    private boolean hasFormLabel(
            Element control,
            Document document
    ) {
        if (!control.attr("aria-label").isBlank()) {
            return true;
        }

        String labelledBy = control.attr("aria-labelledby");

        if (
                !labelledBy.isBlank()
                        && hasValidAriaReference(labelledBy, document)
        ) {
            return true;
        }

        String id = control.id();

        if (!id.isBlank()) {
            Element explicitLabel = document.selectFirst(
                    "label[for=\"" + id + "\"]"
            );

            if (explicitLabel != null) {
                return true;
            }
        }

        Element parent = control.parent();

        while (parent != null) {
            if (parent.tagName().equals("label")) {
                return true;
            }

            parent = parent.parent();
        }

        return false;
    }

    private void checkButtons(
            Document document,
            List<AccessibilityIssue> issues
    ) {
        for (Element button : document.select("button")) {
            if (!hasAccessibleName(button, document)) {
                addIssue(
                        issues,
                        "button-name",
                        "Botão sem nome acessível",
                        "O botão não informa sua finalidade para tecnologias assistivas.",
                        Severity.CRITICAL,
                        "4.1.2",
                        button,
                        "Adicione texto visível, aria-label ou aria-labelledby ao botão."
                );
            }
        }
    }

    private void checkLinks(
            Document document,
            List<AccessibilityIssue> issues
    ) {
        for (Element link : document.select("a[href]")) {
            if (!hasAccessibleName(link, document)) {
                addIssue(
                        issues,
                        "link-name",
                        "Link sem nome acessível",
                        "O link não possui texto que explique seu destino.",
                        Severity.SERIOUS,
                        "2.4.4 e 4.1.2",
                        link,
                        "Adicione texto descritivo, aria-label ou uma imagem com alt apropriado."
                );
            }
        }
    }

    private boolean hasAccessibleName(
            Element element,
            Document document
    ) {
        if (!element.text().isBlank()) {
            return true;
        }

        if (!element.attr("aria-label").isBlank()) {
            return true;
        }

        String labelledBy = element.attr("aria-labelledby");

        if (
                !labelledBy.isBlank()
                        && hasValidAriaReference(labelledBy, document)
        ) {
            return true;
        }

        return element.select("img[alt]")
                .stream()
                .anyMatch(image -> !image.attr("alt").isBlank());
    }

    private void checkAriaReferences(
            Document document,
            List<AccessibilityIssue> issues
    ) {
        checkAriaAttribute(
                document,
                issues,
                "aria-labelledby"
        );

        checkAriaAttribute(
                document,
                issues,
                "aria-describedby"
        );
    }

    private void checkAriaAttribute(
            Document document,
            List<AccessibilityIssue> issues,
            String attribute
    ) {
        for (Element element : document.select("[" + attribute + "]")) {
            String value = element.attr(attribute).trim();

            if (
                    value.isBlank()
                            || !hasValidAriaReference(value, document)
            ) {
                addIssue(
                        issues,
                        "invalid-" + attribute,
                        "Referência ARIA inválida",
                        "O atributo "
                                + attribute
                                + " aponta para um ou mais elementos inexistentes.",
                        Severity.SERIOUS,
                        "4.1.2",
                        element,
                        "Informe IDs existentes e separados por espaço no atributo "
                                + attribute
                                + "."
                );
            }
        }
    }

    private boolean hasValidAriaReference(
            String value,
            Document document
    ) {
        if (value == null || value.isBlank()) {
            return false;
        }

        String[] ids = value.trim().split("\\s+");

        for (String id : ids) {
            if (document.getElementById(id) == null) {
                return false;
            }
        }

        return true;
    }

    private void addIssue(
            List<AccessibilityIssue> issues,
            String ruleId,
            String title,
            String description,
            Severity severity,
            String wcagCriterion,
            Element element,
            String recommendation
    ) {
        issues.add(
                new AccessibilityIssue(
                        ruleId,
                        title,
                        description,
                        severity,
                        wcagCriterion,
                        createSelector(element),
                        createSnippet(element),
                        recommendation
                )
        );
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
                .filter(issue -> issue.severity() == severity)
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
                    + String.join(".", element.classNames());
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
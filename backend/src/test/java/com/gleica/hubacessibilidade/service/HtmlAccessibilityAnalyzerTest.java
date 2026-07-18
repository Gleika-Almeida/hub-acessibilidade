package com.gleica.hubacessibilidade.service;

import com.gleica.hubacessibilidade.dto.AccessibilityIssue;
import com.gleica.hubacessibilidade.dto.ScanReport;
import com.gleica.hubacessibilidade.model.Severity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HtmlAccessibilityAnalyzerTest {

    private HtmlAccessibilityAnalyzer analyzer;

    @BeforeEach
    void setUp() {
        analyzer = new HtmlAccessibilityAnalyzer();
    }

    @Test
    @DisplayName("Deve aprovar um HTML básico sem problemas de acessibilidade")
    void shouldApproveAccessibleHtml() {
        String html = """
                <!DOCTYPE html>
                <html lang="pt-BR">
                    <head>
                        <title>Página acessível</title>
                    </head>

                    <body>
                        <h1>Produtos</h1>

                        <img
                            src="produto.jpg"
                            alt="Produto artesanal"
                        >

                        <label for="email">
                            E-mail
                        </label>

                        <input
                            id="email"
                            type="email"
                        >

                        <button type="button">
                            Abrir menu
                        </button>

                        <a href="/produtos">
                            Ver produtos
                        </a>
                    </body>
                </html>
                """;

        ScanReport report = analyzer.analyze(html);

        assertEquals(100, report.score());
        assertEquals(0, report.summary().total());
        assertTrue(report.issues().isEmpty());
    }

    @Test
    @DisplayName("Deve identificar ausência do idioma da página")
    void shouldReportMissingDocumentLanguage() {
        String html = """
                <!DOCTYPE html>
                <html>
                    <head>
                        <title>Página de teste</title>
                    </head>

                    <body>
                        <h1>Conteúdo principal</h1>
                    </body>
                </html>
                """;

        ScanReport report = analyzer.analyze(html);

        assertTrue(
                containsRule(
                        report.issues(),
                        "html-has-lang"
                )
        );

        AccessibilityIssue issue = findIssue(
                report.issues(),
                "html-has-lang"
        );

        assertEquals(
                Severity.SERIOUS,
                issue.severity()
        );

        assertEquals(
                "3.1.1",
                issue.wcagCriterion()
        );
    }

    @Test
    @DisplayName("Deve identificar ausência do título do documento")
    void shouldReportMissingDocumentTitle() {
        String html = """
                <!DOCTYPE html>
                <html lang="pt-BR">
                    <head></head>

                    <body>
                        <h1>Conteúdo principal</h1>
                    </body>
                </html>
                """;

        ScanReport report = analyzer.analyze(html);

        assertTrue(
                containsRule(
                        report.issues(),
                        "document-title"
                )
        );
    }

    @Test
    @DisplayName("Deve identificar página sem elemento h1")
    void shouldReportMissingHeadingOne() {
        String html = """
                <!DOCTYPE html>
                <html lang="pt-BR">
                    <head>
                        <title>Página de teste</title>
                    </head>

                    <body>
                        <h2>Produtos</h2>
                    </body>
                </html>
                """;

        ScanReport report = analyzer.analyze(html);

        assertTrue(
                containsRule(
                        report.issues(),
                        "page-has-heading-one"
                )
        );
    }

    @Test
    @DisplayName("Deve identificar título vazio")
    void shouldReportEmptyHeading() {
        String html = """
                <!DOCTYPE html>
                <html lang="pt-BR">
                    <head>
                        <title>Página de teste</title>
                    </head>

                    <body>
                        <h1>Produtos</h1>
                        <h2></h2>
                    </body>
                </html>
                """;

        ScanReport report = analyzer.analyze(html);

        assertTrue(
                containsRule(
                        report.issues(),
                        "empty-heading"
                )
        );
    }

    @Test
    @DisplayName("Deve identificar salto na hierarquia de títulos")
    void shouldReportHeadingLevelJump() {
        String html = """
                <!DOCTYPE html>
                <html lang="pt-BR">
                    <head>
                        <title>Página de teste</title>
                    </head>

                    <body>
                        <h1>Produtos</h1>
                        <h3>Produto em destaque</h3>
                    </body>
                </html>
                """;

        ScanReport report = analyzer.analyze(html);

        assertTrue(
                containsRule(
                        report.issues(),
                        "heading-order"
                )
        );
    }

    @Test
    @DisplayName("Deve identificar imagem sem atributo alt")
    void shouldReportImageWithoutAltAttribute() {
        String html = """
                <!DOCTYPE html>
                <html lang="pt-BR">
                    <head>
                        <title>Página de teste</title>
                    </head>

                    <body>
                        <h1>Produtos</h1>

                        <img src="produto.jpg">
                    </body>
                </html>
                """;

        ScanReport report = analyzer.analyze(html);

        assertTrue(
                containsRule(
                        report.issues(),
                        "image-alt"
                )
        );

        AccessibilityIssue issue = findIssue(
                report.issues(),
                "image-alt"
        );

        assertEquals(
                "img",
                issue.selector()
        );

        assertTrue(
                issue.snippet().contains(
                        "produto.jpg"
                )
        );
    }

    @Test
    @DisplayName("Não deve reprovar imagem decorativa com alt vazio")
    void shouldAcceptDecorativeImageWithEmptyAlt() {
        String html = """
                <!DOCTYPE html>
                <html lang="pt-BR">
                    <head>
                        <title>Página de teste</title>
                    </head>

                    <body>
                        <h1>Produtos</h1>

                        <img
                            src="decoracao.svg"
                            alt=""
                        >
                    </body>
                </html>
                """;

        ScanReport report = analyzer.analyze(html);

        assertFalse(
                containsRule(
                        report.issues(),
                        "image-alt"
                )
        );
    }

    @Test
    @DisplayName("Deve identificar campo sem rótulo acessível")
    void shouldReportFormControlWithoutLabel() {
        String html = """
                <!DOCTYPE html>
                <html lang="pt-BR">
                    <head>
                        <title>Cadastro</title>
                    </head>

                    <body>
                        <h1>Cadastro</h1>

                        <input
                            type="email"
                            placeholder="Digite seu e-mail"
                        >
                    </body>
                </html>
                """;

        ScanReport report = analyzer.analyze(html);

        assertTrue(
                containsRule(
                        report.issues(),
                        "form-label"
                )
        );
    }

    @Test
    @DisplayName("Deve aceitar campo associado a um label")
    void shouldAcceptFormControlWithLabel() {
        String html = """
                <!DOCTYPE html>
                <html lang="pt-BR">
                    <head>
                        <title>Cadastro</title>
                    </head>

                    <body>
                        <h1>Cadastro</h1>

                        <label for="email">
                            E-mail
                        </label>

                        <input
                            id="email"
                            type="email"
                        >
                    </body>
                </html>
                """;

        ScanReport report = analyzer.analyze(html);

        assertFalse(
                containsRule(
                        report.issues(),
                        "form-label"
                )
        );
    }

    @Test
    @DisplayName("Deve aceitar campo envolvido por um label")
    void shouldAcceptFormControlInsideLabel() {
        String html = """
                <!DOCTYPE html>
                <html lang="pt-BR">
                    <head>
                        <title>Cadastro</title>
                    </head>

                    <body>
                        <h1>Cadastro</h1>

                        <label>
                            Nome

                            <input type="text">
                        </label>
                    </body>
                </html>
                """;

        ScanReport report = analyzer.analyze(html);

        assertFalse(
                containsRule(
                        report.issues(),
                        "form-label"
                )
        );
    }

    @Test
    @DisplayName("Deve identificar botão sem nome acessível")
    void shouldReportButtonWithoutAccessibleName() {
        String html = """
                <!DOCTYPE html>
                <html lang="pt-BR">
                    <head>
                        <title>Página de teste</title>
                    </head>

                    <body>
                        <h1>Produtos</h1>

                        <button type="button">
                            <svg></svg>
                        </button>
                    </body>
                </html>
                """;

        ScanReport report = analyzer.analyze(html);

        assertTrue(
                containsRule(
                        report.issues(),
                        "button-name"
                )
        );

        AccessibilityIssue issue = findIssue(
                report.issues(),
                "button-name"
        );

        assertEquals(
                Severity.CRITICAL,
                issue.severity()
        );
    }

    @Test
    @DisplayName("Deve aceitar botão com aria-label")
    void shouldAcceptButtonWithAriaLabel() {
        String html = """
                <!DOCTYPE html>
                <html lang="pt-BR">
                    <head>
                        <title>Página de teste</title>
                    </head>

                    <body>
                        <h1>Produtos</h1>

                        <button
                            type="button"
                            aria-label="Abrir menu"
                        >
                            <svg aria-hidden="true"></svg>
                        </button>
                    </body>
                </html>
                """;

        ScanReport report = analyzer.analyze(html);

        assertFalse(
                containsRule(
                        report.issues(),
                        "button-name"
                )
        );
    }

    @Test
    @DisplayName("Deve identificar link sem nome acessível")
    void shouldReportLinkWithoutAccessibleName() {
        String html = """
                <!DOCTYPE html>
                <html lang="pt-BR">
                    <head>
                        <title>Página de teste</title>
                    </head>

                    <body>
                        <h1>Produtos</h1>

                        <a href="/produto"></a>
                    </body>
                </html>
                """;

        ScanReport report = analyzer.analyze(html);

        assertTrue(
                containsRule(
                        report.issues(),
                        "link-name"
                )
        );
    }

    @Test
    @DisplayName("Deve aceitar link com imagem que possui texto alternativo")
    void shouldAcceptLinkWithAlternativeImageText() {
        String html = """
                <!DOCTYPE html>
                <html lang="pt-BR">
                    <head>
                        <title>Página de teste</title>
                    </head>

                    <body>
                        <h1>Produtos</h1>

                        <a href="/produto">
                            <img
                                src="produto.jpg"
                                alt="Ver detalhes do produto"
                            >
                        </a>
                    </body>
                </html>
                """;

        ScanReport report = analyzer.analyze(html);

        assertFalse(
                containsRule(
                        report.issues(),
                        "link-name"
                )
        );

        assertFalse(
                containsRule(
                        report.issues(),
                        "image-alt"
                )
        );
    }

    @Test
    @DisplayName("Deve identificar aria-labelledby apontando para ID inexistente")
    void shouldReportInvalidAriaLabelledByReference() {
        String html = """
                <!DOCTYPE html>
                <html lang="pt-BR">
                    <head>
                        <title>Página de teste</title>
                    </head>

                    <body>
                        <h1>Produtos</h1>

                        <button
                            type="button"
                            aria-labelledby="titulo-inexistente"
                        >
                            <svg></svg>
                        </button>
                    </body>
                </html>
                """;

        ScanReport report = analyzer.analyze(html);

        assertTrue(
                containsRule(
                        report.issues(),
                        "invalid-aria-labelledby"
                )
        );
    }

    @Test
    @DisplayName("Deve aceitar aria-labelledby apontando para ID existente")
    void shouldAcceptValidAriaLabelledByReference() {
        String html = """
                <!DOCTYPE html>
                <html lang="pt-BR">
                    <head>
                        <title>Página de teste</title>
                    </head>

                    <body>
                        <h1>Produtos</h1>

                        <span id="menu-title">
                            Abrir menu
                        </span>

                        <button
                            type="button"
                            aria-labelledby="menu-title"
                        >
                            <svg aria-hidden="true"></svg>
                        </button>
                    </body>
                </html>
                """;

        ScanReport report = analyzer.analyze(html);

        assertFalse(
                containsRule(
                        report.issues(),
                        "invalid-aria-labelledby"
                )
        );

        assertFalse(
                containsRule(
                        report.issues(),
                        "button-name"
                )
        );
    }

    @Test
    @DisplayName("Deve limitar a pontuação mínima a zero")
    void shouldNotReturnNegativeScore() {
        String html = """
                <!DOCTYPE html>
                <html>
                    <head></head>

                    <body>
                        <h3></h3>

                        <img src="foto-1.jpg">
                        <img src="foto-2.jpg">
                        <img src="foto-3.jpg">

                        <input type="text">
                        <input type="email">

                        <button></button>
                        <button></button>

                        <a href="/primeiro"></a>
                        <a href="/segundo"></a>
                    </body>
                </html>
                """;

        ScanReport report = analyzer.analyze(html);

        assertEquals(0, report.score());
        assertTrue(report.summary().total() > 0);
    }

    @Test
    @DisplayName("Deve calcular corretamente o resumo por severidade")
    void shouldCalculateIssueSummaryBySeverity() {
        String html = """
                <!DOCTYPE html>
                <html>
                    <head></head>

                    <body>
                        <h1>Produtos</h1>
                        <h3>Promoções</h3>

                        <button></button>

                        <img src="produto.jpg">
                    </body>
                </html>
                """;

        ScanReport report = analyzer.analyze(html);

        assertEquals(
                report.issues().size(),
                report.summary().total()
        );

        assertEquals(
                countSeverity(
                        report.issues(),
                        Severity.CRITICAL
                ),
                report.summary().critical()
        );

        assertEquals(
                countSeverity(
                        report.issues(),
                        Severity.SERIOUS
                ),
                report.summary().serious()
        );

        assertEquals(
                countSeverity(
                        report.issues(),
                        Severity.MODERATE
                ),
                report.summary().moderate()
        );

        assertEquals(
                countSeverity(
                        report.issues(),
                        Severity.MINOR
                ),
                report.summary().minor()
        );
    }

    private boolean containsRule(
            List<AccessibilityIssue> issues,
            String ruleId
    ) {
        return issues.stream()
                .anyMatch(issue ->
                        issue.ruleId().equals(ruleId)
                );
    }

    private AccessibilityIssue findIssue(
            List<AccessibilityIssue> issues,
            String ruleId
    ) {
        return issues.stream()
                .filter(issue ->
                        issue.ruleId().equals(ruleId)
                )
                .findFirst()
                .orElseThrow(() ->
                        new AssertionError(
                                "A regra não foi encontrada: "
                                        + ruleId
                        )
                );
    }

    private long countSeverity(
            List<AccessibilityIssue> issues,
            Severity severity
    ) {
        return issues.stream()
                .filter(issue ->
                        issue.severity() == severity
                )
                .count();
    }
}
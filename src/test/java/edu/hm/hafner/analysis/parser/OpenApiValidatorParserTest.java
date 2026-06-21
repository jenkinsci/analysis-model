package edu.hm.hafner.analysis.parser;

import java.nio.file.FileSystems;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.FileReaderFactory;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;
import edu.hm.hafner.analysis.registry.ParserRegistry;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link OpenApiValidatorParser}.
 *
 * @author Akash Manna
 */
class OpenApiValidatorParserTest extends AbstractParserTest {
    OpenApiValidatorParserTest() {
        super("openapi-validator-report.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(5);

        softly.assertThat(report.get(0))
                .hasFileName("src/main/openapi/my-service.yaml")
                .hasLineStart(332)
                .hasType("ibm-no-consecutive-path-parameter-segments")
                .hasMessage("Path contains two or more consecutive path parameter references")
                .hasSeverity(Severity.ERROR);

        softly.assertThat(report.get(0).getDescription())
                .contains("paths")
                .contains("/v1/clouds/{cloud_id}/{region_id}");

        softly.assertThat(report.get(1))
                .hasFileName("src/main/openapi/my-service.yaml")
                .hasLineStart(46)
                .hasType("ibm-summary-sentence-style")
                .hasMessage("Operation summaries should not have a trailing period")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(2))
                .hasFileName("src/main/openapi/my-service.yaml")
                .hasLineStart(78)
                .hasType("ibm-operation-id")
                .hasMessage("Each operation should have a non-empty 'operationId'")
                .hasSeverity(Severity.ERROR);

        softly.assertThat(report.get(3))
                .hasFileName("src/main/openapi/my-service.yaml")
                .hasLineStart(5)
                .hasType("info-contact")
                .hasMessage("The 'info' object should contain a 'contact' field")
                .hasSeverity(Severity.WARNING_LOW);

        softly.assertThat(report.get(4))
                .hasFileName("src/main/openapi/models.yaml")
                .hasLineStart(150)
                .hasType("ibm-enum-casing-convention")
                .hasMessage("Enum values should follow snake_case convention")
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Override
    protected IssueParser createParser() {
        return new OpenApiValidatorParser();
    }

    @Test
    void accepts() {
        assertThat(new OpenApiValidatorParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("openapi-validator-report.json")))).isTrue();
        assertThat(new OpenApiValidatorParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("foo.txt")))).isFalse();
    }

    @Test
    void brokenInput() {
        assertThatThrownBy(() -> parse("eclipse.txt"))
                .isInstanceOf(ParsingException.class);
    }

    @Test
    void emptyInput() throws ParsingException {
        var report = parseStringContent("[]");

        assertThat(report).isEmpty();
    }

    @Test
    void shouldHandleAlternateSeverityNames() {
        var report = parse("openapi-validator-edge-cases.json");

        assertThat(report).hasSize(2);

        assertThat(report.get(0))
                .hasFileName("openapi.yaml")
                .hasLineStart(210)
                .hasType("ibm-schema-description")
                .hasMessage("Missing description for schema property")
                .hasSeverity(Severity.WARNING_NORMAL);

        assertThat(report.get(1))
                .hasFileName("openapi.yaml")
                .hasLineStart(99)
                .hasType("ibm-response-status-codes")
                .hasMessage("Response should define a 400 status code")
                .hasSeverity(Severity.WARNING_LOW);
    }

    @Test
    void shouldHandleMissingOptionalFields() {
        var report = parseStringContent("""
                [
                    {
                        "message": "Minimal issue without rule, line or file"
                    }
                ]
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasType("-")
                .hasMessage("Minimal issue without rule, line or file")
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasFileName("-")
                .hasLineStart(0)
                .hasDescription("");
    }

    @Test
    void shouldBuildPathDescription() {
        var report = parseStringContent("""
                [
                    {
                        "message": "Rule with a full API path",
                        "rule": "ibm-path-rule",
                        "path": ["paths", "/v1/users", "get", "parameters"],
                        "line": 55,
                        "severity": "warning",
                        "file": "openapi.yaml"
                    }
                ]
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0).getDescription())
                .contains("paths")
                .contains("/v1/users")
                .contains("get")
                .contains("parameters");
    }

    @Test
    void shouldHandleEmptyPath() {
        var report = parseStringContent("""
                [
                    {
                        "message": "Rule without path",
                        "rule": "ibm-no-path-rule",
                        "path": [],
                        "line": 10,
                        "severity": "error",
                        "file": "openapi.yaml"
                    }
                ]
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0).getDescription()).isEmpty();
    }

    @Test
    void shouldHandleSeverityMapping() {
        var report = parseStringContent("""
                [
                    { "message": "a", "severity": "error" },
                    { "message": "b", "severity": "warning" },
                    { "message": "c", "severity": "warn" },
                    { "message": "d", "severity": "info" },
                    { "message": "e", "severity": "hint" },
                    { "message": "f", "severity": "information" },
                    { "message": "g", "severity": "UNKNOWN" }
                ]
                """);

        assertThat(report).hasSize(7);
        assertThat(report.get(0)).hasSeverity(Severity.ERROR);
        assertThat(report.get(1)).hasSeverity(Severity.WARNING_NORMAL);
        assertThat(report.get(2)).hasSeverity(Severity.WARNING_NORMAL);
        assertThat(report.get(3)).hasSeverity(Severity.WARNING_LOW);
        assertThat(report.get(4)).hasSeverity(Severity.WARNING_LOW);
        assertThat(report.get(5)).hasSeverity(Severity.WARNING_LOW);
        assertThat(report.get(6)).hasSeverity(Severity.WARNING_LOW);
    }

    @Test
    void shouldProvideDescriptorMetadata() {
        var descriptor = new ParserRegistry().get("openapi-validator");

        assertThat(descriptor.getPattern()).isEqualTo("**/openapi-validator-report.json");
        assertThat(descriptor.getHelp()).contains("lint-openapi --json");
        assertThat(descriptor.getUrl()).isEqualTo("https://github.com/IBM/openapi-validator");
        assertThat(descriptor.hasHelp()).isTrue();
        assertThat(descriptor.hasUrl()).isTrue();
    }
}

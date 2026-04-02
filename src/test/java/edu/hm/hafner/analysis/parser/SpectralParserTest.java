package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.FileReaderFactory;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;
import edu.hm.hafner.analysis.registry.ParserRegistry;

import java.nio.file.FileSystems;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link SpectralParser}.
 *
 * @author Akash Manna
 */
class SpectralParserTest extends AbstractParserTest {
    SpectralParserTest() {
        super("spectral-report.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(4);

        softly.assertThat(report.get(0))
                .hasFileName("openapi/petstore.yaml")
                .hasLineStart(12)
                .hasColumnStart(4)
                .hasLineEnd(12)
                .hasColumnEnd(16)
                .hasType("oas3-operation-security-defined")
                .hasMessage("Operation must define security requirements.")
                .hasSeverity(Severity.ERROR);

        softly.assertThat(report.get(1))
                .hasFileName("openapi/petstore.yaml")
                .hasLineStart(2)
                .hasColumnStart(2)
                .hasLineEnd(2)
                .hasColumnEnd(14)
                .hasType("info-contact")
                .hasMessage("Info object should contain contact details.")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(2))
                .hasFileName("openapi/petstore.yaml")
                .hasLineStart(25)
                .hasColumnStart(6)
                .hasLineEnd(25)
                .hasColumnEnd(18)
                .hasType("operation-tags")
                .hasMessage("Operation should have non-empty tags array.")
                .hasSeverity(Severity.WARNING_LOW);

        softly.assertThat(report.get(3))
                .hasFileName("-")
                .hasLineStart(0)
                .hasColumnStart(0)
                .hasLineEnd(0)
                .hasColumnEnd(0)
                .hasType("-")
                .hasMessage("Fallback when rule and range are missing.")
                .hasSeverity(Severity.WARNING_LOW);
    }

    @Override
    protected IssueParser createParser() {
        return new SpectralParser();
    }

    @Test
    void accepts() {
        var parser = new SpectralParser();
        assertThat(parser.accepts(read("spectral-report.json"))).isTrue();
        assertThat(parser.accepts(read("foo.txt"))).isFalse();
    }

    private FileReaderFactory read(final String first) {
        return new FileReaderFactory(FileSystems.getDefault().getPath(first));
    }

    @Test
    void brokenInput() {
        assertThatThrownBy(() -> parse("eclipse.txt"))
                .isInstanceOf(ParsingException.class);
    }

    @Test
    void emptyInput() throws ParsingException {
        var report = parse("issues-no-issues.json");

        assertThat(report).isEmpty();
    }

    @Test
    void shouldHandleWrappedResultsObject() {
        var report = parseStringContent("""
                {
                    "results": [
                        {
                            "code": "operation-description",
                            "message": "Operation should have a description.",
                            "severity": 3,
                            "source": "openapi/petstore.yaml",
                            "range": {
                                "start": {
                                    "line": 30,
                                    "character": 8
                                },
                                "end": {
                                    "line": 30,
                                    "character": 19
                                }
                            }
                        }
                    ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("openapi/petstore.yaml")
                .hasType("operation-description")
                .hasMessage("Operation should have a description.")
                .hasSeverity(Severity.WARNING_LOW)
                .hasLineStart(30)
                .hasColumnStart(8)
                .hasLineEnd(30)
                .hasColumnEnd(19);
    }

    @Test
    void shouldDefaultToWarningForUnknownSeverities() {
        var report = parseStringContent("""
                [
                    {
                        "code": "unmapped-severity",
                        "message": "Unknown string severity should be warning.",
                        "severity": "severe"
                    },
                    {
                        "code": "unmapped-numeric",
                        "message": "Unknown numeric severity should be warning.",
                        "severity": 99
                    }
                ]
                """);

        assertThat(report).hasSize(2);
        assertThat(report.get(0)).hasSeverity(Severity.WARNING_NORMAL);
        assertThat(report.get(1)).hasSeverity(Severity.WARNING_NORMAL);
    }

    @Test
    void shouldProvideDescriptorMetadata() {
        var descriptor = new ParserRegistry().get("spectral");

        assertThat(descriptor.getPattern()).isEqualTo("**/spectral-report.json");
        assertThat(descriptor.getHelp()).contains("spectral lint api.yaml --format json");
        assertThat(descriptor.getUrl()).isEqualTo("https://github.com/stoplightio/spectral");
        assertThat(descriptor.hasHelp()).isTrue();
        assertThat(descriptor.hasUrl()).isTrue();
    }
}
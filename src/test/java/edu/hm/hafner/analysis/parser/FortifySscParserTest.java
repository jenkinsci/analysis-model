package edu.hm.hafner.analysis.parser;

import java.nio.file.FileSystems;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.FileReaderFactory;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Report.IssueType;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;
import edu.hm.hafner.analysis.registry.ParserRegistry;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link FortifySscParser}.
 *
 * @author Akash Manna
 */
class FortifySscParserTest extends AbstractParserTest {
    FortifySscParserTest() {
        super("fortifyssc.json");
    }

    @Override
    protected IssueParser createParser() {
        return new FortifySscParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(3);

        softly.assertThat(report.get(0))
                .hasFileName("/src/main/java/com/example/LoginController.java")
                .hasLineStart(42)
                .hasType("SQL Injection")
                .hasMessage("SQL Injection")
                .hasSeverity(Severity.WARNING_HIGH);

        softly.assertThat(report.get(1))
                .hasFileName("/src/main/java/com/example/LoginController.java")
                .hasLineStart(84)
                .hasType("Cross-Site Scripting")
                .hasMessage("Cross-Site Scripting")
                .hasSeverity(Severity.WARNING_LOW);

        softly.assertThat(report.get(2))
                .hasFileName("src/main/java/com/example/Resource.java")
                .hasLineStart(100)
                .hasType("Unreleased Resource")
                .hasMessage("Unreleased Resource")
                .hasSeverity(Severity.ERROR);
    }

    @Test
    void accepts() {
        assertThat(new FortifySscParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("fortifyssc.json")))).isTrue();
        assertThat(new FortifySscParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("foo.txt")))).isFalse();
    }

    @Test
    void brokenInput() {
        assertThatThrownBy(() -> parse("eclipse.txt"))
                .isInstanceOf(ParsingException.class);
    }

    @Test
    void shouldHandleMissingDataKey() {
        var report = parseStringContent("""
                {
                    "count": 0
                }
                """);
        assertThat(report).isEmpty();
    }

    @Test
    void shouldHandleMissingLocation() {
        var report = parseStringContent("""
                {
                  "data": [
                    {
                      "id": 12345,
                      "issueName": "SQL Injection",
                      "friority": "High"
                    }
                  ]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("-")
                .hasLineStart(0)
                .hasType("SQL Injection")
                .hasMessage("SQL Injection")
                .hasSeverity(Severity.WARNING_HIGH);
    }

    @Test
    void shouldProvideDescriptorMetadata() {
        var descriptor = new ParserRegistry().get("fortifyssc");

        assertThat(descriptor.getPattern()).isEqualTo("**/fortifyssc.json");
        assertThat(descriptor.getUrl()).isEqualTo("https://www.microfocus.com/en-us/cyberres/application-security/software-security-center");
        assertThat(descriptor.getType()).isEqualTo(IssueType.VULNERABILITY);
    }
}
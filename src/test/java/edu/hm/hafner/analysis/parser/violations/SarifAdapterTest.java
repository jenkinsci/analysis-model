package edu.hm.hafner.analysis.parser.violations;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link SarifAdapter}.
 *
 * @author Ullrich Hafner
 */
class SarifAdapterTest extends AbstractParserTest {
    SarifAdapterTest() {
        super("sarif.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(2);
        softly.assertThat(report.get(0))
                .hasFileName("/whatever/path.c")
                .hasLineStart(123)
                .hasType("Cyclomatic complexity")
                .hasSeverity(Severity.WARNING_HIGH);
        softly.assertThat(report.get(0).getMessage()).matches("asdasd");
        softly.assertThat(report.get(1))
                .hasFileName("/whatever/path.c")
                .hasLineStart(123)
                .hasType("-")
                .hasSeverity(Severity.WARNING_LOW);
        softly.assertThat(report.get(1).getMessage()).matches("asdasd");
    }

    @Test
    void shouldReportDifferentSeverities() {
        var report = parse("security-scan.sarif");

        assertThat(report).hasSize(51);
        assertThat(report.getSizeOf(Severity.WARNING_HIGH)).isEqualTo(3);
        assertThat(report.getSizeOf(Severity.WARNING_NORMAL)).isEqualTo(45);
        assertThat(report.getSizeOf(Severity.WARNING_LOW)).isEqualTo(3);
    }

    @Test
    void handleFilePathsInFileURIschemeFormat() {
        var report = parse("filePathInFileUriScheme.sarif");
        try (var softly = new SoftAssertions()) {
            softly.assertThat(report).hasSize(2);
            assertThatReportHasSeverities(report, 0, 0, 2, 0);
            softly.assertThat(report.get(0))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(6)
                    .hasLineEnd(6)
                    .hasFileName("C:/my/workspace/project/this/dir/file.cs");
            softly.assertThat(report.get(1))
                    .hasSeverity(Severity.WARNING_NORMAL)
                    .hasLineStart(5)
                    .hasLineEnd(5)
                    .hasFileName("C:/my/workspace/project/whatever/€path.cs");
        }
    }

    @ParameterizedTest(name = "[{index}] Filename with invalid path: {0}")
    @ValueSource(strings = {"brokenfilePath.sarif", "emptyfilePath.sarif"})
    void handleBrokenPathsInFileURIschemeFormat(final String fileName) {
        var report = parse(fileName);
        try (var softly = new SoftAssertions()) {
            softly.assertThat(report).hasSize(2);
            assertThatReportHasSeverities(report, 0, 0, 2, 0);
            softly.assertThat(report.get(0).getFileName()).endsWith("this/dir/file.cs");
            softly.assertThat(report.get(1).getFileName()).endsWith("path.cs");
        }
    }

    @Test
    void shouldIgnoreSuppressedIssues() {
        var report = parse("suppressed-sarif.json");
        try (var softly = new SoftAssertions()) {
            softly.assertThat(report).hasSize(2);
            softly.assertThat(report.get(0))
                .hasFileName("/whatever/path.c")
                .hasLineStart(123)
                .hasType("Cyclomatic complexity")
                .hasSeverity(Severity.WARNING_HIGH);
            softly.assertThat(report.get(0).getMessage()).matches("asdasd");
            softly.assertThat(report.get(1))
                .hasFileName("/whatever/path.c")
                .hasLineStart(123)
                .hasType("abcdef")
                .hasSeverity(Severity.WARNING_LOW);
            softly.assertThat(report.get(1).getMessage()).matches("asdasd");
        }
    }

    @Override
    protected SarifAdapter createParser() {
        return new SarifAdapter();
    }
}

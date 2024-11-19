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

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link HadoLintParser}.
 *
 * @author Andreas Mandel
 */
class HadoLintParserTest extends AbstractParserTest {
    HadoLintParserTest() {
        super("hadolint.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(5);
        softly.assertThat(report.get(0))
                .hasSeverity(Severity.WARNING_LOW);
        softly.assertThat(report.get(1))
                .hasLineStart(15)
                .hasCategory("DL3008")
                .hasMessage("Pin versions in apt get install. Instead of `apt-get install <package>` use `apt-get install <package>=<version>`")
                .hasColumnStart(1)
                .hasFileName("Dockerfile")
                .hasSeverity(Severity.WARNING_HIGH);
        softly.assertThat(report.get(2))
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Override
    protected IssueParser createParser() {
        return new HadoLintParser();
    }

    @Test
    void accepts() {
        assertThat(new HadoLintParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("lint.json")))).isTrue();
        assertThat(new HadoLintParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("foo.txt")))).isFalse();
    }

    @Test
    void unusualInput() {
        var report = parse("hadolint-unsual.json");
        assertThat(report).hasSize(4);
        assertThat(report.get(1))
                .hasSeverity(Severity.ERROR);
        assertThat(report.get(2))
                .hasSeverity(Severity.WARNING_LOW)
                .hasCategory("DL3008");
        assertThat(report.get(3))
                .hasFileName("Dockerfile")
                .hasMessage("Avoid additional packages by specifying `--no-install-recommends`")
                .hasColumnStart(1);
    }

    @Test
    void brokenInput() {
        assertThatThrownBy(() -> parse("eclipse.txt"))
                .isInstanceOf(ParsingException.class);
    }
}

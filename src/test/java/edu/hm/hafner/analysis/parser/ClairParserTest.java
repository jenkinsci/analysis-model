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
 * Tests the class {@link ClairParser}.
 *
 * @author Andreas Mandel
 */
class ClairParserTest extends AbstractParserTest {
    ClairParserTest() {
        super("clair.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(112);
        softly.assertThat(report.get(1)).hasSeverity(Severity.WARNING_LOW);
        softly.assertThat(report.get(0))
                .hasMessage(
                        "sqlite3:3.22.0-1ubuntu0.1 ext/fts3/fts3.c in SQLite before 3.32.0 has a use-after-free "
                                + "in fts3EvalNextRow, related to the snippet feature. Fixed by 3.22.0-1ubuntu0.4 "
                                + "see http://people.ubuntu.com/~ubuntu-security/cve/CVE-2020-13630")
                .hasCategory("CVE-2020-13630")
                .hasSeverity(Severity.WARNING_LOW)
                .hasType("ubuntu:18.04")
                .hasFileName("registry.example.com/project/project-docker-was/develop:10.0.230");
    }

    @Override
    protected IssueParser createParser() {
        return new ClairParser();
    }

    @Test
    void accepts() {
        assertThat(new ClairParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("clair.json")))).isTrue();
        assertThat(new ClairParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("clair.xml")))).isFalse();
    }

    @Test
    void unusualInput() {
        var report = parse("clair-unusual.json");
        assertThat(report).hasSize(5);
        assertThat(report.get(0))
                .hasSeverity(Severity.WARNING_LOW)
                .hasMessage(
                        "sqlite3 Fixed by 3.22.0-1ubuntu0.4");
        assertThat(report.get(1))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasMessage(
                        "libidn2");
        assertThat(report.get(2))
                .hasSeverity(Severity.ERROR);
        assertThat(report.get(3))
                .hasMessage(
                        "libgcrypt20 see http://people.ubuntu.com/~ubuntu-security/cve/CVE-2019-13627");
    }

    @Test
    void brokenInput() {
        assertThatThrownBy(() -> parse("eclipse.txt"))
                .isInstanceOf(ParsingException.class);
    }
}

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
 * Tests the class {@link OTDockerLintParser}.
 *
 * @author Abhishek Dubey
 */
class OTDockerLintParserTest extends AbstractParserTest {
    OTDockerLintParserTest() {
        super("ot-docker-linter.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(3);
        softly.assertThat(report.get(0))
                .hasModuleName("WORKDIR spsp/")
                .hasCategory("DL3000")
                .hasDescription("Use absolute WORKDIR.")
                .hasLineStart(3)
                .hasFileName("testing/Dockerfile.testing")
                .hasSeverity(Severity.ERROR);
        softly.assertThat(report.get(2))
                .hasSeverity(Severity.WARNING_LOW);
        softly.assertThat(report.get(1))
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Override
    protected IssueParser createParser() {
        return new OTDockerLintParser();
    }

    @Test
    void accepts() {
        assertThat(new OTDockerLintParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("lint.json")))).isTrue();
        assertThat(new OTDockerLintParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("foo.txt")))).isFalse();
    }

    @Test
    void brokenInput() {
        assertThatThrownBy(() -> parse("eclipse.txt"))
                .isInstanceOf(ParsingException.class);
    }
}

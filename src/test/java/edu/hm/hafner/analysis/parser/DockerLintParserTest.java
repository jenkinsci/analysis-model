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
 * Tests the class {@link DockerLintParser}.
 *
 * @author Andreas Mandel
 */
class DockerLintParserTest extends AbstractParserTest {
    DockerLintParserTest() {
        super("dockerlint.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(7);
        softly.assertThat(report.get(1))
                .hasMessage(
                        "Invalid parameters for command. See https://docs.docker.com/engine/reference/builder/")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasLineStart(39)
                .hasCategory("")
                .hasFileName("Dockerfile");
        softly.assertThat(report.get(4))
                .hasCategory("maintainer_deprecated");
        softly.assertThat(report.get(5))
                .hasMessage(
                    "There is no 'EXPOSE' instruction - Without exposed ports how will the service of the "
                        + "container be accessed? See https://docs.docker.com/engine/reference/builder/#expose");
        softly.assertThat(report.get(0)).hasLineStart(37);
    }

    @Override
    protected IssueParser createParser() {
        return new DockerLintParser();
    }

    @Test
    void brokenInput() {
        assertThatThrownBy(() -> parse("eclipse.txt"))
                .isInstanceOf(ParsingException.class);
    }

    @Test
    void accepts() {
        assertThat(new DockerLintParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("dockerlint.json")))).isTrue();
        assertThat(new DockerLintParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("foo.txt")))).isFalse();
    }
}

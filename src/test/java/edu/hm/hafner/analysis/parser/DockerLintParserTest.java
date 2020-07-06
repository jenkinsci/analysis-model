package edu.hm.hafner.analysis.parser;

import java.nio.file.FileSystems;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.FileReaderFactory;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.assertions.SoftAssertions;

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
        assertThat(report).hasSize(7);
        assertThat(report.get(1)).hasFileName("Dockerfile");
        assertThat(report.get(1)).hasMessage(
                "Invalid parameters for command. See https://docs.docker.com/engine/reference/builder/");
        assertThat(report.get(5)).hasMessage(
                "There is no 'EXPOSE' instruction - Without exposed ports how will the service of the "
                        + "container be accessed? See https://docs.docker.com/engine/reference/builder/#expose");
        assertThat(report.get(0)).hasLineStart(37);
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
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
 * Tests the class {@link MarkdownLintParser}.
 * 
 * @author Akash Manna
 */
class MarkdownLintParserTest extends AbstractParserTest {
    MarkdownLintParserTest() {
        super("markdownlint.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(4);

        softly.assertThat(report.get(0))
                .hasFileName("README.md")
                .hasLineStart(1)
                .hasColumnStart(1)
                .hasColumnEnd(1)
                .hasType("MD041")
                .hasMessage("First line in file should be a top level heading")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(1))
                .hasFileName("README.md")
                .hasLineStart(7)
                .hasColumnStart(81)
                .hasColumnEnd(89)
                .hasType("MD013")
                .hasMessage("Line length: Expected: 80; Actual: 88")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(2))
                .hasFileName("docs/usage.md")
                .hasLineStart(5)
                .hasColumnStart(1)
                .hasColumnEnd(2)
                .hasType("MD012")
                .hasMessage("Multiple consecutive blank lines")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(3))
                .hasFileName("docs/usage.md")
                .hasLineStart(12)
                .hasType("-")
                .hasMessage("Missing language for fenced code block")
                .hasSeverity(Severity.WARNING_NORMAL);
    }

    @Override
    protected IssueParser createParser() {
        return new MarkdownLintParser();
    }

    @Test
    void accepts() {
        assertThat(new MarkdownLintParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("markdownlint.json")))).isTrue();
        assertThat(new MarkdownLintParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("foo.txt")))).isFalse();
    }

    @Test
    void brokenInput() {
        assertThatThrownBy(() -> parse("eclipse.txt"))
                .isInstanceOf(ParsingException.class);
    }
}
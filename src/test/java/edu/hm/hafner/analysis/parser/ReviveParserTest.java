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
 * Tests the class {@link ReviveParser}.
 *
 * @author Akash Manna
 */
class ReviveParserTest extends AbstractParserTest {
    ReviveParserTest() {
        super("revive-report.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(6);

        // Verify we have issues from three different files
        var mainGoIssues = report.stream().filter(i -> i.getFileName().equals("main.go")).toList();
        var handlersGoIssues = report.stream().filter(i -> i.getFileName().equals("handlers.go")).toList();
        var utilsGoIssues = report.stream().filter(i -> i.getFileName().equals("utils.go")).toList();

        softly.assertThat(mainGoIssues).hasSize(3);
        softly.assertThat(handlersGoIssues).hasSize(2);
        softly.assertThat(utilsGoIssues).hasSize(1);

        // Verify main.go issues
        softly.assertThat(mainGoIssues.get(0))
                .hasFileName("main.go")
                .hasLineStart(10)
                .hasColumnStart(6)
                .hasType("add-constant")
                .hasMessage("string literal \"app-name\" has more than 3 occurrences, make it a named constant")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(mainGoIssues.get(1))
                .hasFileName("main.go")
                .hasLineStart(25)
                .hasColumnStart(2)
                .hasType("var-naming")
                .hasMessage("variable name 'MyVar' should not contain underscores")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(mainGoIssues.get(2))
                .hasFileName("main.go")
                .hasLineStart(35)
                .hasColumnStart(1)
                .hasType("blank-imports")
                .hasMessage("imported for side-effects only")
                .hasSeverity(Severity.ERROR);

        // Verify handlers.go issues
        softly.assertThat(handlersGoIssues.get(0))
                .hasFileName("handlers.go")
                .hasLineStart(42)
                .hasColumnStart(18)
                .hasType("unused-parameter")
                .hasMessage("parameter 'ctx' seems to be unused")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(handlersGoIssues.get(1))
                .hasFileName("handlers.go")
                .hasLineStart(55)
                .hasColumnStart(5)
                .hasType("receiver-naming")
                .hasMessage("receiver name 'this' should be abbreviated")
                .hasSeverity(Severity.WARNING_NORMAL);

        // Verify utils.go issues
        softly.assertThat(utilsGoIssues.get(0))
                .hasFileName("utils.go")
                .hasLineStart(12)
                .hasColumnStart(1)
                .hasType("exported")
                .hasMessage("exported function RunServer should have comment or be unexported")
                .hasSeverity(Severity.WARNING_LOW);
    }

    @Override
    protected IssueParser createParser() {
        return new ReviveParser();
    }

    @Test
    void accepts() {
        assertThat(new ReviveParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("revive-report.json")))).isTrue();
        assertThat(new ReviveParser().accepts(
                new FileReaderFactory(FileSystems.getDefault().getPath("foo.txt")))).isFalse();
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
    void shouldHandleEmptyReportFile() throws ParsingException {
        var report = parseStringContent("{}");

        assertThat(report).isEmpty();
    }

    @Test
    void shouldHandleEmptyIssuesArray() throws ParsingException {
        var report = parseStringContent("""
                {
                  "main.go": []
                }
                """);

        assertThat(report).isEmpty();
    }

    @Test
    void shouldSkipNonObjectEntries() throws ParsingException {
        var report = parseStringContent("""
                {
                  "main.go": [null, {"line":1, "column":2, "rule":"r", "message":"m", "severity":"error"}]
                }
                """);

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("main.go")
                .hasLineStart(1)
                .hasColumnStart(2)
                .hasType("r")
                .hasMessage("m")
                .hasSeverity(Severity.ERROR);
    }
}

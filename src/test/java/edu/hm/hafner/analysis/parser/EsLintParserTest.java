package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;
import org.junit.jupiter.api.Test;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link EsLintParser}.
 *
 *  @author Ulrich Grave
 */
class EsLintParserTest extends AbstractParserTest {
    EsLintParserTest() {
        super("eslint/eslint.json");
    }

    @Override
    protected IssueParser createParser() {
        return new EsLintParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(2);

        softly.assertThat(report.get(0))
                .hasFileName("/var/lib/jenkins/workspace/eslint/fullOfProblems.js")
                .hasType("no-unused-vars")
                .hasLineStart(1)
                .hasColumnStart(10)
                .hasLineEnd(1)
                .hasColumnEnd(16)
                .hasMessage("'addOne' is defined but never used.")
                .hasDescription("")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(1))
                .hasFileName("/var/lib/jenkins/workspace/eslint/fullOfProblems.js")
                .hasType("use-isnan")
                .hasLineStart(2)
                .hasColumnStart(9)
                .hasLineEnd(2)
                .hasColumnEnd(17)
                .hasMessage("Use the isNaN function to compare with NaN.")
                .hasDescription("<p>Suggestions:</p> <ul><li><p>Replace with Number.isNaN.</p> <pre><code>!Number.isNaN(i)</code></pre></li><li><p>Replace with Number.isNaN and cast to a Number.</p> <pre><code>!Number.isNaN(Number(i))</code></pre></li></ul>")
                .hasSeverity(Severity.ERROR);
    }

    @Test
    void shouldAcceptJsonFiles() {
        assertThat(createParser().accepts(createReaderFactory("eslint/eslint.json"))).isTrue();
        assertThat(createParser().accepts(createReaderFactory("eslint/eslint-checkstyle.xml"))).isFalse();
    }

    @Test
    void shouldReportWithMetadata() {
        var report = parse("eslint/eslint-with-metadata.json");
        try (var softAssertions = new SoftAssertions()) {
            assertThatIssuesArePresent(report, softAssertions);
        }
    }
}
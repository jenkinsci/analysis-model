package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

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
        softly.assertThat(report).hasSize(7);

        softly.assertThat(report.get(0))
                .hasFileName("/var/lib/jenkins/workspace/eslint/fullOfProblems.js")
                .hasType("no-unused-vars")
                .hasLineStart(1)
                .hasColumnStart(10)
                .hasLineEnd(1)
                .hasColumnEnd(16)
                .hasMessage("'addOne' is defined but never used.")
                .hasDescription("<p>Suggestions:</p> <ul><li><p>Remove unused variable &#x27;addOne&#x27;.</p></li></ul>")
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

        softly.assertThat(report.get(2))
                .hasFileName("/var/lib/jenkins/workspace/eslint/fullOfProblems.js")
                .hasType("consistent-return")
                .hasLineStart(5)
                .hasColumnStart(7)
                .hasLineEnd(5)
                .hasColumnEnd(13)
                .hasMessage("Function 'addOne' expected a return value.")
                .hasDescription("")
                .hasSeverity(Severity.ERROR);

        softly.assertThat(report.get(3))
                .hasFileName("/var/lib/jenkins/workspace/eslint/fullOfProblems.js")
                .hasType("indent")
                .hasLineStart(5)
                .hasColumnStart(1)
                .hasLineEnd(5)
                .hasColumnEnd(7)
                .hasMessage("Expected indentation of 8 spaces but found 6.")
                .hasDescription("")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(4))
                .hasFileName("/var/lib/jenkins/workspace/eslint/fullOfProblems.js")
                .hasType("semi")
                .hasLineStart(3)
                .hasColumnStart(20)
                .hasLineEnd(4)
                .hasColumnEnd(1)
                .hasMessage("Missing semicolon.")
                .hasDescription("<p>Fix:</p> <pre><code>;</code></pre>")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(5))
                .hasFileName("/var/lib/jenkins/workspace/eslint/fullOfProblems.js")
                .hasType("None")
                .hasLineStart(0)
                .hasColumnStart(0)
                .hasLineEnd(0)
                .hasColumnEnd(0)
                .hasMessage("n/a")
                .hasDescription("")
                .hasSeverity(Severity.ERROR);

        softly.assertThat(report.get(6))
                .hasFileName("/var/lib/jenkins/workspace/eslint/fullOfProblems.js")
                .hasType("None")
                .hasLineStart(0)
                .hasColumnStart(0)
                .hasLineEnd(0)
                .hasColumnEnd(0)
                .hasMessage("no keys")
                .hasDescription("")
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

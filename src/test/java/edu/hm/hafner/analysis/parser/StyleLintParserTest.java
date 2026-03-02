package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

/**
 * Tests the class {@link StyleLintParser}.
 *
 *  @author Ulrich Grave
 */
class StyleLintParserTest extends AbstractParserTest {
    StyleLintParserTest() {
        super("stylelint/stylelint.json");
    }

    @Override
    protected IssueParser createParser() {
        return new StyleLintParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(4);

        softly.assertThat(report.get(0))
                .hasFileName("/var/lib/jenkins/workspace/stylelint/problem.css")
                .hasType("selector-class-pattern")
                .hasLineStart(20)
                .hasColumnStart(1)
                .hasLineEnd(29)
                .hasColumnEnd(10)
                .hasMessage("Expected \".my-class\" to match pattern \"^(allowed)-([a-z][a-z0-9]*)((-|__)[a-z0-9]+)*$\" (selector-class-pattern)")
                .hasDescription("")
                .hasSeverity(Severity.WARNING_NORMAL);

        softly.assertThat(report.get(1))
                .hasFileName("/var/lib/jenkins/workspace/stylelint/problem.css")
                .hasType("unit-allowed-list")
                .hasLineStart(35)
                .hasColumnStart(1)
                .hasLineEnd(35)
                .hasColumnEnd(12)
                .hasMessage("Unexpected unit \"%\" (unit-allowed-list)")
                .hasDescription("")
                .hasSeverity(Severity.ERROR);

        softly.assertThat(report.get(2))
                .hasFileName("/var/lib/jenkins/workspace/stylelint/problem.css")
                .hasType("declaration-block-no-duplicate-properties")
                .hasLineStart(30)
                .hasColumnStart(5)
                .hasLineEnd(30)
                .hasColumnEnd(10)
                .hasMessage("Unexpected duplicate \"color\" (declaration-block-no-duplicate-properties)")
                .hasDescription("")
                .hasSeverity(Severity.ERROR);

        softly.assertThat(report.get(3))
                .hasFileName("/var/lib/jenkins/workspace/stylelint/problem.css")
                .hasType("selector-not-notation")
                .hasLineStart(27)
                .hasColumnStart(2)
                .hasLineEnd(27)
                .hasColumnEnd(18)
                .hasMessage("Expected simple :not() pseudo-class notation (selector-not-notation)")
                .hasDescription("<p>Fix:</p> <pre><code>):not(</code></pre>")
                .hasSeverity(Severity.ERROR);
    }
}

package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

import static edu.hm.hafner.analysis.assertj.Assertions.*;

/**
 * Tests the class {@link ErrorProneParser}.
 *
 * @author Ullrich Hafner
 */
class ErrorProneParserTest extends AbstractParserTest {
    ErrorProneParserTest() {
        super("errorprone-maven.log");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(9);

        softly.assertThat(report.get(0))
                .hasFileName("/Users/hafner/Development/git/analysis-model/src/main/java/edu/hm/hafner/analysis/IssueBuilder.java")
                .hasLineStart(86)
                .hasColumnStart(74)
                .hasType("NullAway")
                .hasMessage("passing @Nullable parameter 'fileName' where @NonNull is required")
                .hasDescription("<p><a href=\"http://t.uber.com/nullaway\">See ErrorProne documentation.</a></p>");
        softly.assertThat(report.get(2))
                .hasFileName("/Users/hafner/Development/git/analysis-model/src/main/java/edu/hm/hafner/analysis/parser/DrMemoryParser.java")
                .hasLineStart(193)
                .hasColumnStart(44)
                .hasType("StringSplitter")
                .hasMessage("String.split(String) has surprising behavior")
                .hasDescription("Did you mean: "
                        + "<pre><code>"
                        + "for (String line : Splitter.onPattern(&quot;\\\\r?\\\\n&quot;).split(stackTrace)) {"
                        + "</code></pre>"
                        + "<p><a href=\"https://errorprone.info/bugpattern/StringSplitter\">See ErrorProne documentation.</a></p>");
    }

    @Override
    protected IssueParser createParser() {
        return new ErrorProneParser();
    }
}

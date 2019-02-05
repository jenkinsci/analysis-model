package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

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

    @Test
    void shouldFindAllWarnings() {
        Report report = parse("error-prone-maven.log");

        assertThat(report).hasSize(1);
        assertThat(report.get(0))
                .hasFileName("/Users/hafner/Development/jenkins/workspace/Model - Freestyle - New/src/main/java/edu/hm/hafner/analysis/parser/RobocopyParser.java")
                .hasLineStart(29)
                .hasColumnStart(45)
                .hasType("StringSplitter")
                .hasMessage("String.split(String) has surprising behavior")
                .hasDescription("Did you mean: <pre><code>String file = matcher.group(4).split(&quot;\\\\s{11}&quot;, -1)[0];</code></pre><p><a href=\"http://errorprone.info/bugpattern/StringSplitter\">See ErrorProne documentation.</a></p>");
    }

    @Override
    protected IssueParser createParser() {
        return new ErrorProneParser();
    }
}

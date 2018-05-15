package edu.hm.hafner.analysis.parser.violations;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.Report;
import static edu.hm.hafner.analysis.assertj.Assertions.assertThat;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link ErrorProneAdapter}.
 *
 * @author Ullrich Hafner
 */
class ErrorProneAdapterTest extends AbstractParserTest {
    ErrorProneAdapterTest() {
        super("error-prone.log");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(5);
        softly.assertThat(report.get(0))
                .hasMessage("Prefer Splitter to String.split\n"
                        + "\n"
                        + "for (final String part : link.split(\",\")) "
                        + "{^(see http://errorprone.info/bugpattern/StringSplitter)Did you mean "
                        + "'for (final String part : Splitter.on(\",\").split(link)) {'?")
                .hasFileName("/home/bjerre/workspace/git-changelog/git-changelog-lib/src/main/java/se/bjurr/gitchangelog/internal/integrations/github/GitHubHelper.java")
                .hasType("StringSplitter")
                .hasLineStart(51)
                .hasPriority(Priority.NORMAL);
    }

    @Override
    protected AbstractParser createParser() {
        return new ErrorProneAdapter();
    }

    @Test @Disabled("See https://github.com/tomasbjerre/violations-lib/issues/37")
    void shouldProvideDetailsWhenParsingMavenLog() {
        Report report = parse("error-prone-maven.log");

        assertThat(report).hasSize(1);
    }
}
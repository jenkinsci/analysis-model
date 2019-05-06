package edu.hm.hafner.analysis.parser;

import static edu.hm.hafner.analysis.assertj.IssuesAssert.assertThat;

import java.util.Iterator;
import java.util.UUID;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.LineRange;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link JsonParser}.
 */
class JsonParserTest extends AbstractParserTest {

    JsonParserTest() {
        super("json-issues.log");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(3);
        Iterator<Issue> iterator = report.iterator();

        Issue issue1 = iterator.next();
        softly.assertThat(issue1)
                .hasFileName("d/file.txt")
                .containsExactlyLineRanges(new LineRange(10, 11), new LineRange(20, 21))
                .hasCategory("c")
                .hasDescription("d")
                .hasType("t")
                .hasSeverity(Severity.WARNING_LOW)
                .hasMessage("msg")
                .hasPackageName("pn")
                .hasModuleName("mdl")
                .hasOrigin("orgn")
                .hasReference("ref")
                .hasFingerprint("fgpt")
                .hasAdditionalProperties("ap")
                .hasId(UUID.fromString("823b92b6-98eb-41c4-83ce-b6ec1ed6f98f"));

        Issue issue2 = iterator.next();
        softly.assertThat(issue2)
                .hasFileName("test.txt")
                .hasLineStart(10)
                .hasLineEnd(11)
                .hasColumnStart(110)
                .hasColumnEnd(120)
                .hasDescription("some description")
                .hasSeverity(Severity.ERROR)
                .hasMessage("some message");

        Issue issue3 = iterator.next();
        softly.assertThat(issue3)
                .hasFileName("test.txt")
                .containsExactlyLineRanges(new LineRange(10, 10), new LineRange(220, 220))
                .hasDescription("an \"important\" description")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasMessage("an \"important\" message");

        softly.assertThat(iterator.hasNext()).isFalse();
    }

    @Override
    protected JsonParser createParser() {
        return new JsonParser();
    }
}

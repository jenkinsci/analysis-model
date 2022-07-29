package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests the class {@link FlowParser}.
 */
class FlowParserTest extends StructuredFileParserTest {
    private static final String CATEGORY = DEFAULT_CATEGORY;
    private static final String FILENAME = "flow.json";

    protected FlowParserTest() {
        super(FILENAME);
    }

    @Override
    protected IssueParser createParser() {
        return new FlowParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(1);
        softly.assertThat(report.get(0))
                .hasFileName("src/app.js")
                .hasSeverity(Severity.ERROR)
                .hasCategory(CATEGORY)
                .hasType("infer")
                .hasMessage("Cannot call `server.serve` with `80` bound to `port` because number [1] is incompatible with string [2]. [incompatible-call]")
                .hasLineStart(12)
                .hasLineEnd(12)
                .hasColumnStart(18)
                .hasColumnEnd(19);
    }

    @Test
    void shouldAcceptJsonFile() {
        assertThat(createParser().accepts(createReaderFactory(FILENAME))).isTrue();
    }
}

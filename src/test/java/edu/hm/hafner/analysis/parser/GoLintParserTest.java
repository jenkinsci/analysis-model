package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.AbstractIssueParserTest;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.IssuesAssert.*;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link GoLintParser}.
 */
class GoLintParserTest extends AbstractIssueParserTest {
    GoLintParserTest() {
        super("golint.txt");
    }

    @Override
    protected GoLintParser createParser() {
        return new GoLintParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(7);

        softly.assertThat(report.get(0))
                .hasPriority(Priority.NORMAL)
                .hasLineStart(64)
                .hasLineEnd(64)
                .hasMessage("exported var ErrCloseSent should have comment or be unexported")
                .hasFileName("conn.go");

        softly.assertThat(report.get(1))
                .hasPriority(Priority.NORMAL)
                .hasLineStart(104)
                .hasLineEnd(104)
                .hasMessage("should replace pos += 1 with pos++")
                .hasFileName("conn.go");

        softly.assertThat(report.get(2))
                .hasPriority(Priority.NORMAL)
                .hasLineStart(305)
                .hasLineEnd(305)
                .hasMessage("should replace c.writeSeq += 1 with c.writeSeq++")
                .hasFileName("conn.go");

        softly.assertThat(report.get(3))
                .hasPriority(Priority.NORMAL)
                .hasLineStart(360)
                .hasLineEnd(360)
                .hasMessage("should replace c.writeSeq += 1 with c.writeSeq++")
                .hasFileName("conn.go");

        softly.assertThat(report.get(4))
                .hasPriority(Priority.NORMAL)
                .hasLineStart(669)
                .hasLineEnd(669)
                .hasMessage("should replace c.readSeq += 1 with c.readSeq++")
                .hasFileName("conn.go");

        softly.assertThat(report.get(5))
                .hasPriority(Priority.NORMAL)
                .hasLineStart(706)
                .hasLineEnd(706)
                .hasMessage("should replace r.c.readSeq += 1 with r.c.readSeq++")
                .hasFileName("conn.go");

        softly.assertThat(report.get(6))
                .hasPriority(Priority.NORMAL)
                .hasLineStart(18)
                .hasLineEnd(18)
                .hasMessage("should omit type net.Error from declaration of var timeoutErrImplementsNetError; it will be inferred from the right-hand side")
                .hasFileName("conn_test.go");
    }
}
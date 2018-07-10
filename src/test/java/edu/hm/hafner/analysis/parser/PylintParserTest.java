package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import edu.hm.hafner.analysis.AbstractIssueParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.Report;
import static edu.hm.hafner.analysis.assertj.IssuesAssert.*;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link PyLintParser}.
 */
class PylintParserTest extends AbstractIssueParserTest {
    private static final String ISSUES_FILE = "pyLint.txt";

    /**
     * Creates a new instance of {@link PylintParserTest}.
     */
    PylintParserTest() {
        super(ISSUES_FILE);
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(6);

        Iterator<Issue> iterator = report.iterator();

        softly.assertThat(iterator.next())
                .hasLineStart(3)
                .hasLineEnd(3)
                .hasMessage("Line too long (85/80)")
                .hasFileName("trunk/src/python/cachedhttp.py")
                .hasCategory("C")
                .hasPriority(Priority.LOW);

        softly.assertThat(iterator.next())
                .hasLineStart(28)
                .hasLineEnd(28)
                .hasMessage("Invalid name \"seasonCount\" (should match [a-z_][a-z0-9_]{2,30}$)")
                .hasFileName("trunk/src/python/tv.py")
                .hasCategory("C0103")
                .hasPriority(Priority.LOW);

        softly.assertThat(iterator.next())
                .hasLineStart(35)
                .hasLineEnd(35)
                .hasMessage("Missing docstring")
                .hasFileName("trunk/src/python/tv.py")
                .hasCategory("C0111")
                .hasPriority(Priority.LOW);

        softly.assertThat(iterator.next())
                .hasLineStart(39)
                .hasLineEnd(39)
                .hasMessage("Method should have \"self\" as first argument")
                .hasFileName("trunk/src/python/tv.py")
                .hasCategory("E0213")
                .hasPriority(Priority.HIGH);

        softly.assertThat(iterator.next())
                .hasLineStart(5)
                .hasLineEnd(5)
                .hasMessage("Unable to import 'deadbeef'")
                .hasFileName("trunk/src/python/tv.py")
                .hasCategory("F0401")
                .hasPriority(Priority.HIGH);

        softly.assertThat(iterator.next())
                .hasLineStart(39)
                .hasLineEnd(39)
                .hasMessage("Dangerous default value \"[]\" as argument")
                .hasFileName("trunk/src/python/tv.py")
                .hasCategory("W0102")
                .hasPriority(Priority.NORMAL);
    }

    @Override
    protected PyLintParser createParser() {
        return new PyLintParser();
    }
}

package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueBuilder;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.IssuesAssert.*;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link AcuCobolParser}.
 */
public class AcuCobolParserTest extends AbstractParserTest {
    private static final String CATEGORY = new IssueBuilder().build().getCategory();

    AcuCobolParserTest() {
        super("acu.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Issues<Issue> issues, final SoftAssertions softly) {
        assertThat(issues).hasSize(4);

        Iterator<Issue> iterator = issues.iterator();
        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory(CATEGORY)
                .hasLineStart(39)
                .hasLineEnd(39)
                .hasMessage("Imperative statement required")
                .hasFileName("COPY/zzz.CPY");
        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory(CATEGORY)
                .hasLineStart(111)
                .hasLineEnd(111)
                .hasMessage("Don't run with knives")
                .hasFileName("C:/Documents and Settings/xxxx/COB/bbb.COB");
        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory(CATEGORY)
                .hasLineStart(115)
                .hasLineEnd(115)
                .hasMessage("Don't run with knives")
                .hasFileName("C:/Documents and Settings/xxxx/COB/bbb.COB");
        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory(CATEGORY)
                .hasLineStart(123)
                .hasLineEnd(123)
                .hasMessage("I'm a green banana")
                .hasFileName("C:/Documents and Settings/xxxx/COB/ccc.COB");
    }

    @Override
    protected AbstractParser createParser() {
        return new AcuCobolParser();
    }
}


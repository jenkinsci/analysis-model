package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;
import static edu.hm.hafner.analysis.assertj.IssuesAssert.*;

/**
 * Tests the class {@link PREfastParser}.
 *
 * @author Charles Chan
 */
public class PREfastParserTest extends AbstractParserTest {

    PREfastParserTest() {
        super("PREfast.xml");
    }

    @Override
    protected void assertThatIssuesArePresent(final Issues<Issue> issues, final SoftAssertions softly) {
        assertThat(issues).hasSize(11);

        Iterator<Issue> iterator = issues.iterator();

        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory("28101")
                .hasLineStart(102)
                .hasLineEnd(102)
                .hasMessage(
                        "The Drivers module has inferred that the current function is a DRIVER_INITIALIZE function:  This is informational only. No problem has been detected.")
                .hasFileName("sys.c");

        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory("6014")
                .hasLineStart(116)
                .hasLineEnd(116)
                .hasMessage("(PFD)Leaking memory 'device'.")
                .hasFileName("sys.c");

        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory("28155")
                .hasLineStart(137)
                .hasLineEnd(137)
                .hasMessage(
                        "The function being assigned or passed should be a DRIVER_UNLOAD function:  Add the declaration 'DRIVER_UNLOAD OnUnload;' before the current first declaration of OnUnload.")
                .hasFileName("sys.c");
    }

    /**
     * Creates the parser.
     *
     * @return the warnings parser
     */
    @Override
    protected AbstractParser createParser() {
        return new PREfastParser();
    }
}

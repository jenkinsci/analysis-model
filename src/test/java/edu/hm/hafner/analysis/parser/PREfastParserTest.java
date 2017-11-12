package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.IssuesAssert.*;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

/**
 * Tests the class {@link PREfastParser}.
 *
 * @author Charles Chan
 */
public class PREfastParserTest extends ParserTester {
    private static final String TYPE = new PREfastParser().getId();

    /**
     * Tests the Puppet-Lint parsing.
     */
    @Test
    public void testParse() {
        Issues<Issue> results = new PREfastParser().parse(openFile());
        assertThat(results).hasSize(11);

        Iterator<Issue> iterator = results.iterator();

        assertSoftly(softly -> {
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
        });
    }

    @Override
    protected String getWarningsFile() {
        return "PREfast.xml";
    }
}

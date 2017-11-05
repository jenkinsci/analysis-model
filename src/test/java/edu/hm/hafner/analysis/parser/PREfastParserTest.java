package edu.hm.hafner.analysis.parser;

import java.io.IOException;
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
     *
     * @throws IOException in case of an error
     */
    @Test
    public void testParse() throws IOException {
        Issues results = new PREfastParser().parse(openFile());
        assertThat(results).hasSize(11);

        Iterator<Issue> iterator = results.iterator();
        checkWarnings(iterator.next(), Priority.NORMAL, "28101", 102,
                "The Drivers module has inferred that the current function is a DRIVER_INITIALIZE function:  This is informational only. No problem has been detected.", "sys.c", TYPE);

        checkWarnings(iterator.next(), Priority.NORMAL, "6014", 116,
                "(PFD)Leaking memory 'device'.", "sys.c", TYPE);

        checkWarnings(iterator.next(), Priority.NORMAL, "28155", 137,
                "The function being assigned or passed should be a DRIVER_UNLOAD function:  Add the declaration 'DRIVER_UNLOAD OnUnload;' before the current first declaration of OnUnload.", "sys.c", TYPE);
    }

    private void checkWarnings(Issue issue, Priority priority, String category, int lineStartAndEnd, String message, String fileName, String type) {
        assertSoftly(softly -> softly.assertThat(issue)
                .hasPriority(priority)
                .hasCategory(category)
                .hasLineStart(lineStartAndEnd)
                .hasLineEnd(lineStartAndEnd)
                .hasMessage(message)
                .hasFileName(fileName)
                .hasType(type)
        );
    }

    @Override
    protected String getWarningsFile() {
        return "PREfast.xml";
    }
}

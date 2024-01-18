package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link PreFastParser}.
 *
 * @author Charles Chan
 */
class PreFastParserTest extends AbstractParserTest {
    PreFastParserTest() {
        super("PREfast.xml");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(11);

        Iterator<Issue> iterator = report.iterator();

        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("28101")
                .hasLineStart(102)
                .hasLineEnd(102)
                .hasMessage(
                        "The Drivers module has inferred that the current function is a DRIVER_INITIALIZE function:  This is informational only. No problem has been detected.")
                .hasFileName("sys.c");

        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("6014")
                .hasLineStart(116)
                .hasLineEnd(116)
                .hasMessage("(PFD)Leaking memory 'device'.")
                .hasFileName("sys.c");

        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_NORMAL)
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
    protected PreFastParser createParser() {
        return new PreFastParser();
    }
}

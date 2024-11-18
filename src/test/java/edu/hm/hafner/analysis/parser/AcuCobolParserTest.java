package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link AcuCobolParser}.
 */
class AcuCobolParserTest extends AbstractParserTest {
    AcuCobolParserTest() {
        super("acu.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(4);

        Iterator<Issue> iterator = report.iterator();
        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(39)
                .hasMessage("Imperative statement required")
                .hasFileName("COPY/zzz.CPY");
        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(111)
                .hasMessage("Don't run with knives")
                .hasFileName("C:/Documents and Settings/xxxx/COB/bbb.COB");
        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(115)
                .hasMessage("Don't run with knives")
                .hasFileName("C:/Documents and Settings/xxxx/COB/bbb.COB");
        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(123)
                .hasMessage("I'm a green banana")
                .hasFileName("C:/Documents and Settings/xxxx/COB/ccc.COB");
    }

    @Override
    protected AcuCobolParser createParser() {
        return new AcuCobolParser();
    }
}

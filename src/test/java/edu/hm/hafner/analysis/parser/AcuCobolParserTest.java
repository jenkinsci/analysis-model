package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.IssuesAssert.*;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

/**
 * Tests the class {@link AcuCobolParser}.
 */
public class AcuCobolParserTest extends ParserTester {
    /**
     * Parses a file with 4 COBOL warnings.
     */
    @Test
    public void parseFile() {
        Issues<Issue> warnings = new AcuCobolParser().parse(openFile());

        assertThat(warnings).hasSize(4);

        Iterator<Issue> iterator = warnings.iterator();
        assertSoftly(softly -> {
            softly.assertThat(iterator.next())
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(DEFAULT_CATEGORY)
                    .hasLineStart(39)
                    .hasLineEnd(39)
                    .hasMessage("Imperative statement required")
                    .hasFileName("COPY/zzz.CPY");
            softly.assertThat(iterator.next())
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(DEFAULT_CATEGORY)
                    .hasLineStart(111)
                    .hasLineEnd(111)
                    .hasMessage("Don't run with knives")
                    .hasFileName("C:/Documents and Settings/xxxx/COB/bbb.COB");
            softly.assertThat(iterator.next())
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(DEFAULT_CATEGORY)
                    .hasLineStart(115)
                    .hasLineEnd(115)
                    .hasMessage("Don't run with knives")
                    .hasFileName("C:/Documents and Settings/xxxx/COB/bbb.COB");
            softly.assertThat(iterator.next())
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(DEFAULT_CATEGORY)
                    .hasLineStart(123)
                    .hasLineEnd(123)
                    .hasMessage("I'm a green banana")
                    .hasFileName("C:/Documents and Settings/xxxx/COB/ccc.COB");
        });
    }

    @Override
    protected String getWarningsFile() {
        return "acu.txt";
    }
}


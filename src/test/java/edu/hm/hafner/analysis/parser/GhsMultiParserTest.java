package edu.hm.hafner.analysis.parser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;
import edu.hm.hafner.analysis.assertj.IssuesAssert;

/**
 * Tests the class {@link GhsMultiParser}.
 */
public class GhsMultiParserTest extends ParserTester {
    private static final String TYPE = new GhsMultiParser().getId();

    /**
     * Parses a file with two deprecation warnings.
     *
     * @throws IOException if the file could not be read
     */
    @Test
    public void parseMultiLine() throws IOException {
        Issues warnings = new GhsMultiParser().parse(openFile());
        IssuesAssert.assertThat(warnings).hasSize(3);

        Iterator<Issue> iterator = warnings.iterator();
        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory("#546-D")
                .hasLineStart(37)
                .hasLineEnd(37)
                .hasMessage("transfer of control bypasses initialization of:\n            variable \"CF_TRY_FLAG\" (declared at line 42)\n            variable \"CF_EXCEPTION_NOT_CAUGHT\" (declared at line 42)\n        CF_TRY_CHECK_EX(ex2);")
                .hasFileName("/maindir/tests/TestCase_0101.cpp\"")
                .hasType(TYPE);

        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory("#177-D")
                .hasLineStart(29)
                .hasLineEnd(29)
                .hasMessage("label\n          \"CF_TRY_LABELex1\" was declared but never referenced\n     CF_TRY_EX(ex1)")
                .hasFileName("/maindir/tests/TestCase_0101.cpp\"")
                .hasType(TYPE);

        softly.assertThat(iterator.next())
                .hasPriority(Priority.NORMAL)
                .hasCategory("#381-D")
                .hasLineStart(9)
                .hasLineEnd(9)
                .hasMessage("extra\n          \";\" ignored\n  TEST_DSS( CHECK_4TH_CONFIG_DATA, 18, 142, 'F');")
                .hasFileName("/maindir/tests/TestCase_1601.cpp\"")
                .hasType(TYPE);
        softly.assertAll();
    }

    @Override
    protected String getWarningsFile() {
        return "ghsmulti.txt";
    }
}


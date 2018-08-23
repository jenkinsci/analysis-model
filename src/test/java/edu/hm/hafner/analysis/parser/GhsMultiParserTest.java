package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.AbstractIssueParserTest;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link GhsMultiParser}.
 */
class GhsMultiParserTest extends AbstractIssueParserTest {
    GhsMultiParserTest() {
        super("ghsmulti.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(4);
        softly.assertThat(report.get(0))
                .hasPriority(Priority.NORMAL)
                .hasCategory("#546-D")
                .hasLineStart(37)
                .hasMessage("transfer of control bypasses initialization of:\n            variable \"CF_TRY_FLAG\" (declared at line 42)\n            variable \"CF_EXCEPTION_NOT_CAUGHT\" (declared at line 42)\n        CF_TRY_CHECK_EX(ex2);")
                .hasFileName("/maindir/tests/TestCase_0101.cpp");

        softly.assertThat(report.get(1))
                .hasPriority(Priority.NORMAL)
                .hasCategory("#177-D")
                .hasLineStart(29)
                .hasMessage("label\n          \"CF_TRY_LABELex1\" was declared but never referenced\n     CF_TRY_EX(ex1)")
                .hasFileName("/maindir/tests/TestCase_0101.cpp");

        softly.assertThat(report.get(2))
                .hasPriority(Priority.NORMAL)
                .hasCategory("#381-D")
                .hasLineStart(9)
                .hasMessage("extra\n          \";\" ignored\n  TEST_DSS( CHECK_4TH_CONFIG_DATA, 18, 142, 'F');")
                .hasFileName("/maindir/tests/TestCase_1601.cpp");
        
        softly.assertThat(report.get(3))
                .hasPriority(Priority.NORMAL)
                .hasCategory("#177-D")
                .hasLineStart(23)
                .hasMessage("variable \"myvar\" was declared but never referenced\n  static const uint32 myvar")
                .hasFileName("/workspace/TEST/mytest.c");
        
        
    }

    @Override
    protected GhsMultiParser createParser() {
        return new GhsMultiParser();
    }
}


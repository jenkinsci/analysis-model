package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;

/**
 * Tests the class {@link CrossCoreEmbeddedStudioParser}.
 */
class CrossCoreEmbeddedStudioParserTest extends AbstractParserTest {
    CrossCoreEmbeddedStudioParserTest() {
        super("cces.log");
    }

    @Override
    protected CrossCoreEmbeddedStudioParser createParser() {
        return new CrossCoreEmbeddedStudioParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        // check all warnings were caught
        softly.assertThat(report).hasSize(6);


        // test in details the first warning
        softly.assertThat(report.get(0))
                .hasFileName("src/dummy_1.c")
                .hasLineStart(333)
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("cc0188")
                .hasMessage("enumerated type mixed with another type");

        // test in details the last warning, that has column (but not parsed)
        softly.assertThat(report.get(5))
            .hasFileName("src/dummy_5.c")
            .hasLineStart(125)
            .hasSeverity(Severity.WARNING_NORMAL)
            .hasCategory("cc1462")
            .hasMessage("call to dummy_btc has not been inlined");

    }
}

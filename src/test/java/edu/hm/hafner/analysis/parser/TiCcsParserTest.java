package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

/**
 * Tests the class {@link TiCcsParser}.
 */
class TiCcsParserTest extends AbstractParserTest {
    private static final String UNKNOWN_FILE = "-";

    TiCcsParserTest() {
        super("ticcs.txt");
    }

    @Override
    protected TiCcsParser createParser() {
        return new TiCcsParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(10);

        softly.assertThat(report.get(0))
                .hasSeverity(Severity.WARNING_LOW)
                .hasCategory("#880-D")
                .hasLineStart(341)
                .hasLineEnd(341)
                .hasMessage("parameter \"params\" was never referenced")
                .hasFileName("C:/SCM/Lr/src/fxns.c");
        softly.assertThat(report.get(1))
                .hasSeverity(Severity.WARNING_LOW)
                .hasCategory("#1116-D")
                .hasLineStart(177)
                .hasLineEnd(177)
                .hasMessage("may want to suffix float constant with an f")
                .hasFileName("C:/SCM/Lr/src/edge.c");
        softly.assertThat(report.get(2))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("symbol 'memset' redeclared with incompatible type")
                .hasFileName(UNKNOWN_FILE);
        softly.assertThat(report.get(3))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(12)
                .hasLineEnd(12)
                .hasMessage("variable \"h\" was declared but never referenced")
                .hasFileName("i2cDisplay12x2.c");
        softly.assertThat(report.get(4))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(2578)
                .hasLineEnd(2578)
                .hasMessage("variable")
                .hasFileName("C:/DOCUME~1/JLINNE~1/LOCALS~1/Temp/0360811");
        softly.assertThat(report.get(5))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasLineStart(11)
                .hasLineEnd(11)
                .hasMessage("expected a \";\"")
                .hasFileName("i2cDisplay12x2.c");
        softly.assertThat(report.get(6))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("unresolved symbols remain")
                .hasFileName(UNKNOWN_FILE);
        softly.assertThat(report.get(7))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("errors encountered during linking; \"../../bin/Debug/lrxyz.out\" not")
                .hasFileName(UNKNOWN_FILE);
        softly.assertThat(report.get(8))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasLineStart(3)
                .hasLineEnd(3)
                .hasMessage("could not open source file \"i2cDisplay12x12.h\"")
                .hasFileName("i2cDisplay12x2.c");
        softly.assertThat(report.get(9))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasLineStart(5)
                .hasLineEnd(5)
                .hasMessage("[E0002] Illegal mnemonic specified")
                .hasFileName("foo.asm");
    }
}


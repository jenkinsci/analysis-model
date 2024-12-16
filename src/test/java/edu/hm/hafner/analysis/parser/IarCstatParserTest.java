package edu.hm.hafner.analysis.parser;

import java.util.Iterator;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

import static edu.hm.hafner.analysis.assertions.Assertions.*;

/**
 * Tests the class {@link IarCstatParser}.
 *
 * @author Lorenz Aebi
 */
class IarCstatParserTest extends AbstractParserTest {
    IarCstatParserTest() {
        super("iar-cstat.txt");
    }

    @Override
    protected IarCstatParser createParser() {
        return new IarCstatParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(6).hasDuplicatesSize(0);

        Iterator<Issue> iterator = report.iterator();
        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_LOW)
                .hasCategory("MISRAC2012-Rule-14.4_c")
                .hasLineStart(129)
                .hasLineEnd(129)
                .hasMessage("Type of if condition is not boolean. MISRAC2012-Rule-14.4")
                .hasFileName("src/main/hal/HalAdc.c");
        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_LOW)
                .hasCategory("MISRAC2012-Rule-8.4")
                .hasLineStart(145)
                .hasLineEnd(145)
                .hasMessage(
                        "Definition of externally-linked `ADC0_IRQHandler()' has no compatible declaration. MISRAC2012-Rule-8.4")
                .hasFileName("src/main/hal/HalAdcLS.c");
        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("MISRAC2012-Rule-14.3_a")
                .hasLineStart(131)
                .hasLineEnd(131)
                .hasMessage(
                        "Conditional expression `0!=PalLog_GetUpdateRate()' is always true. CERT-EXP17-C,MISRAC2012-Rule-14.3")
                .hasFileName("src/main/pal/PalLog.c");
        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory("PTR-null-fun-pos")
                .hasLineStart(15)
                .hasLineEnd(15)
                .hasMessage(
                        "Function call `f1()' is immediately dereferenced, without checking for NULL. CERT-EXP34-C,CWE-476")
                .hasFileName("cstat1.c");
        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_LOW)
                .hasCategory("RED-unused-assign")
                .hasLineStart(18)
                .hasLineEnd(18)
                .hasMessage("Value assigned to variable `ch' is never used. CERT-MSC13-C,CWE-563")
                .hasFileName("cstat1.c");
        softly.assertThat(iterator.next())
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory("ARR-inv-index")
                .hasLineStart(16)
                .hasLineEnd(16)
                .hasMessage(
                        "Array `arr' 1st subscript 20 is out of bounds [0,9]. CERT-ARR33-C,CWE-119,CWE-120,CWE-121,CWE-124,CWE-126,CWE-127,CWE-129,MISRAC++2008-5-0-16,MISRAC2012-Rule-18.1")
                .hasFileName("cstat2.c");
    }
}

package edu.hm.hafner.analysis.parser.pvsstudio;

import java.util.Locale;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

/**
 * Tests the class {@link PVSStudioParser}.
 *
 * @author PVS-Studio Team
 */
class PVSStudioParserTest extends AbstractParserTest {
    PVSStudioParserTest() {
        super("TestReport.plog");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(33);
        softly.assertThat(report.getFiles()).hasSize(1);

        assertThatReportHasSeverities(report, 1, 5, 24, 3);

        softly.assertThat(report.filter(Issue.byType(AnalyzerType.GENERAL_MESSAGE)).getSize()).isEqualTo(7);
        softly.assertThat(report.filter(Issue.byType(AnalyzerType.OPTIMIZATION_MESSAGE)).getSize()).isEqualTo(1);
        softly.assertThat(report.filter(Issue.byType(AnalyzerType.CUSTOMER_SPECIFIC_MESSAGE)).getSize()).isEqualTo(3);
        softly.assertThat(report.filter(Issue.byType(AnalyzerType.VIVA_64_MESSAGE)).getSize()).isEqualTo(11);
        softly.assertThat(report.filter(Issue.byType(AnalyzerType.MISRA_MESSAGE)).getSize()).isEqualTo(9);
        softly.assertThat(report.filter(Issue.byType(AnalyzerType.UNKNOWN_MESSAGE)).getSize()).isEqualTo(2);

        softly.assertThat(report.get(0))
                .hasSeverity(Severity.ERROR)
                .hasCategory("V002")
                .hasLineStart(42)
                .hasMessage(getFormedMessage("V002", "Some diagnostic messages may contain incorrect line number."))
                .hasFileName("D:/PartPath/PartPath/out/test/resources/TestReport.plog");
        softly.assertThat(report.get(1))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory("V106")
                .hasLineStart(42)
                .hasMessage(getFormedMessage("V106",
                        "Implicit type conversion third argument '(lstrlenA(Source) + 1)' of function 'memmove' to memsize type."))
                .hasFileName("D:/PartPath/PartPath/out/test/resources/TestReport.plog");
    }

    private String getFormedMessage(final String type, final String messageFromFile) {
        return "<a target=\"_blank\" href=\"https://pvs-studio.com/en/docs/warnings/" + type.toLowerCase(Locale.ENGLISH) + "/\">"
                + type + "</a> " + messageFromFile;
    }

    @Override
    protected IssueParser createParser() {
        return new PVSStudioParser();
    }
}

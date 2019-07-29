package edu.hm.hafner.analysis.parser;

import java.util.Locale;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

import edu.hm.hafner.analysis.parser.pvsstudio.PVSStudioParser;

/**
 * Tests the class {@link PVSStudioParser}.
 *
 * @author PVS-Studio Team
 */
class PVSStudioParserTest extends AbstractParserTest {
    private static final String PREFIX = "pvsstudio/";

    PVSStudioParserTest() {
        super(PREFIX + "TestReport.plog");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(33);
        softly.assertThat(report.getFiles()).hasSize(2);

        softly.assertThat(report).hasSeverities(1, 5, 24, 3);

        softly.assertThat(report.filter(Issue.byType("General Analysis")).getSize()).isEqualTo(7);
        softly.assertThat(report.filter(Issue.byType("Micro-optimization")).getSize()).isEqualTo(1);
        softly.assertThat(report.filter(Issue.byType("Specific Requests")).getSize()).isEqualTo(3);
        softly.assertThat(report.filter(Issue.byType("64-bit")).getSize()).isEqualTo(11);
        softly.assertThat(report.filter(Issue.byType("MISRA")).getSize()).isEqualTo(9);
        softly.assertThat(report.filter(Issue.byType("Unknown")).getSize()).isEqualTo(2);

        softly.assertThat(report.get(0))
                .hasSeverity(Severity.ERROR)
                .hasCategory("V002")
                .hasLineStart(42)
                .hasMessage(getFormedMessage("V002", "Some diagnostic messages may contain incorrect line number."))
                .hasFileName("C:/Users/Komarov/IdeaProjects/warnings-ng-plugin/work/workspace/fgh/ABackup/Lib/CppLib/KitCpp.h");
        softly.assertThat(report.get(1))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasCategory("V106")
                .hasLineStart(42)
                .hasMessage(getFormedMessage("V106", "Implicit type conversion third argument '(lstrlenA(Source) + 1)' of function 'memmove' to memsize type."))
                .hasFileName("C:/Users/Komarov/IdeaProjects/warnings-ng-plugin/work/workspace/fgh/ABackup/Lib/CppLib/KitCpp.h");
    }

    private String getFormedMessage(final String type, final String messageFromFile) {
        return  "<a target=\"_blank\" href=\"https://www.viva64.com/en/w/" + type.toLowerCase(Locale.ENGLISH) + "/\">"
                + type + "</a> " + messageFromFile;
    }

    @Override
    protected IssueParser createParser() {
        return new PVSStudioParser();
    }
}

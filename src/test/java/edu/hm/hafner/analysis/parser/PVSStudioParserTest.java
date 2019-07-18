package edu.hm.hafner.analysis.parser;

import java.util.Locale;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

import edu.hm.hafner.analysis.parser.pvsstudio.PVSStudioParser;

class PVSStudioParserTest extends AbstractParserTest {
    private static final String PREFIX = "pvsstudio/";

    PVSStudioParserTest() {
        super(PREFIX + "TestReport.plog");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(28);
        softly.assertThat(report.getFiles()).hasSize(2);

        softly.assertThat(report.stream().map(Issue::getSeverity).filter(s -> s == Severity.ERROR).count()).isEqualTo(1);
        softly.assertThat(report.stream().map(Issue::getSeverity).filter(s -> s == Severity.WARNING_HIGH).count()).isEqualTo(5);
        softly.assertThat(report.stream().map(Issue::getSeverity).filter(s -> s == Severity.WARNING_NORMAL).count()).isEqualTo(19);
        softly.assertThat(report.stream().map(Issue::getSeverity).filter(s -> s == Severity.WARNING_LOW).count()).isEqualTo(3);

        softly.assertThat(report.stream().map(Issue::getType).filter("GENERAL Analysis"::equals).count()).isEqualTo(4);
        softly.assertThat(report.stream().map(Issue::getType).filter("Micro-OPTIMIZATION"::equals).count()).isEqualTo(0);
        softly.assertThat(report.stream().map(Issue::getType).filter("Specific Requests"::equals).count()).isEqualTo(3);
        softly.assertThat(report.stream().map(Issue::getType).filter("64-bit"::equals).count()).isEqualTo(11);
        softly.assertThat(report.stream().map(Issue::getType).filter("Misra"::equals).count()).isEqualTo(9);
        softly.assertThat(report.stream().map(Issue::getType).filter("Unknown"::equals).count()).isEqualTo(1);

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

package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;
import edu.hm.hafner.analysis.registry.AbstractParserTest;

/**
 * Tests the class {@link SunCParser}.
 */
class SunCParserTest extends AbstractParserTest {
    private static final String MESSAGE = "String literal converted to char* in formal argument 1 in call to userlog(char*, ...).";
    private static final String CATEGORY = "badargtypel2w";

    SunCParserTest() {
        super("sunc.txt");
    }

    @Override
    protected SunCParser createParser() {
        return new SunCParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(8);

        softly.assertThat(report.get(0))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(CATEGORY)
                .hasLineStart(212)
                .hasLineEnd(212)
                .hasMessage(MESSAGE)
                .hasFileName("usi_plugin.cpp");
        softly.assertThat(report.get(1))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(CATEGORY)
                .hasLineStart(224)
                .hasLineEnd(224)
                .hasMessage("String literal converted to char* in formal argument msg in call to except(char*).")
                .hasFileName("usi_plugin.cpp");
        softly.assertThat(report.get(2))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasLineStart(8)
                .hasLineEnd(8)
                .hasMessage(MESSAGE)
                .hasFileName("ServerList.cpp");
        softly.assertThat(report.get(3))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(CATEGORY)
                .hasLineStart(44)
                .hasLineEnd(44)
                .hasMessage(MESSAGE)
                .hasFileName("ServerList.cpp");
        softly.assertThat(report.get(4))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory(CATEGORY)
                .hasLineStart(50)
                .hasLineEnd(50)
                .hasMessage(MESSAGE)
                .hasFileName("ServerList.cpp");
        softly.assertThat(report.get(5))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("hidef")
                .hasLineStart(19)
                .hasLineEnd(19)
                .hasMessage("Child::operator== hides the function Parent::operator==(Parent&) const.")
                .hasFileName("warner.cpp");
        softly.assertThat(report.get(6))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasCategory("wbadlkgasg")
                .hasLineStart(30)
                .hasLineEnd(30)
                .hasMessage("Assigning void(*)(int) to extern \"C\" void(*)(int).")
                .hasFileName("warner.cpp");
        softly.assertThat(report.get(7))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasLineStart(32)
                .hasLineEnd(32)
                .hasMessage("statement is unreachable.")
                .hasFileName("warner.cpp");
    }
}

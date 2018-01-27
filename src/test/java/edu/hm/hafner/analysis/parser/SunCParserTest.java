package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

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
    protected AbstractParser createParser() {
        return new SunCParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Issues<Issue> issues, final SoftAssertions softly) {
        softly.assertThat(issues).hasSize(8);

        softly.assertThat(issues.get(0))
                .hasPriority(Priority.NORMAL)
                .hasCategory(CATEGORY)
                .hasLineStart(212)
                .hasLineEnd(212)
                .hasMessage(MESSAGE)
                .hasFileName("usi_plugin.cpp");
        softly.assertThat(issues.get(1))
                .hasPriority(Priority.NORMAL)
                .hasCategory(CATEGORY)
                .hasLineStart(224)
                .hasLineEnd(224)
                .hasMessage("String literal converted to char* in formal argument msg in call to except(char*).")
                .hasFileName("usi_plugin.cpp");
        softly.assertThat(issues.get(2))
                .hasPriority(Priority.HIGH)
                .hasLineStart(8)
                .hasLineEnd(8)
                .hasMessage(MESSAGE)
                .hasFileName("ServerList.cpp");
        softly.assertThat(issues.get(3))
                .hasPriority(Priority.NORMAL)
                .hasCategory(CATEGORY)
                .hasLineStart(44)
                .hasLineEnd(44)
                .hasMessage(MESSAGE)
                .hasFileName("ServerList.cpp");
        softly.assertThat(issues.get(4))
                .hasPriority(Priority.NORMAL)
                .hasCategory(CATEGORY)
                .hasLineStart(50)
                .hasLineEnd(50)
                .hasMessage(MESSAGE)
                .hasFileName("ServerList.cpp");
        softly.assertThat(issues.get(5))
                .hasPriority(Priority.NORMAL)
                .hasCategory("hidef")
                .hasLineStart(19)
                .hasLineEnd(19)
                .hasMessage("Child::operator== hides the function Parent::operator==(Parent&) const.")
                .hasFileName("warner.cpp");
        softly.assertThat(issues.get(6))
                .hasPriority(Priority.NORMAL)
                .hasCategory("wbadlkgasg")
                .hasLineStart(30)
                .hasLineEnd(30)
                .hasMessage("Assigning void(*)(int) to extern \"C\" void(*)(int).")
                .hasFileName("warner.cpp");
        softly.assertThat(issues.get(7))
                .hasPriority(Priority.NORMAL)
                .hasLineStart(32)
                .hasLineEnd(32)
                .hasMessage("statement is unreachable.")
                .hasFileName("warner.cpp");
    }
}


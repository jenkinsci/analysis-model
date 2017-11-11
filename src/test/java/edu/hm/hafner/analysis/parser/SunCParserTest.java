package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.Assertions.*;
import static edu.hm.hafner.analysis.assertj.SoftAssertions.*;

/**
 * Tests the class {@link SunCParser}.
 */
public class SunCParserTest extends ParserTester {
    private static final String TYPE = new SunCParser().getId();
    private static final String MESSAGE = "String literal converted to char* in formal argument 1 in call to userlog(char*, ...).";
    private static final String CATEGORY = "badargtypel2w";

    /**
     * Parses a file with 8 warnings.
     */
    @Test
    public void parseSunCpp() {
        Issues<Issue> warnings = new SunCParser().parse(openFile());

        assertThat(warnings).hasSize(8);

        assertSoftly(softly -> {
            softly.assertThat(warnings.get(0))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(CATEGORY)
                    .hasLineStart(212)
                    .hasLineEnd(212)
                    .hasMessage(MESSAGE)
                    .hasFileName("usi_plugin.cpp");
            softly.assertThat(warnings.get(1))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(CATEGORY)
                    .hasLineStart(224)
                    .hasLineEnd(224)
                    .hasMessage("String literal converted to char* in formal argument msg in call to except(char*).")
                    .hasFileName("usi_plugin.cpp");
            softly.assertThat(warnings.get(2))
                    .hasPriority(Priority.HIGH)
                    .hasCategory(DEFAULT_CATEGORY)
                    .hasLineStart(8)
                    .hasLineEnd(8)
                    .hasMessage(MESSAGE)
                    .hasFileName("ServerList.cpp");
            softly.assertThat(warnings.get(3))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(CATEGORY)
                    .hasLineStart(44)
                    .hasLineEnd(44)
                    .hasMessage(MESSAGE)
                    .hasFileName("ServerList.cpp");
            softly.assertThat(warnings.get(4))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(CATEGORY)
                    .hasLineStart(50)
                    .hasLineEnd(50)
                    .hasMessage(MESSAGE)
                    .hasFileName("ServerList.cpp");
            softly.assertThat(warnings.get(5))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory("hidef")
                    .hasLineStart(19)
                    .hasLineEnd(19)
                    .hasMessage("Child::operator== hides the function Parent::operator==(Parent&) const.")
                    .hasFileName("warner.cpp");
            softly.assertThat(warnings.get(6))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory("wbadlkgasg")
                    .hasLineStart(30)
                    .hasLineEnd(30)
                    .hasMessage("Assigning void(*)(int) to extern \"C\" void(*)(int).")
                    .hasFileName("warner.cpp");
            softly.assertThat(warnings.get(7))
                    .hasPriority(Priority.NORMAL)
                    .hasCategory(DEFAULT_CATEGORY)
                    .hasLineStart(32)
                    .hasLineEnd(32)
                    .hasMessage("statement is unreachable.")
                    .hasFileName("warner.cpp");
        });
    }

    @Override
    protected String getWarningsFile() {
        return "sunc.txt";
    }
}


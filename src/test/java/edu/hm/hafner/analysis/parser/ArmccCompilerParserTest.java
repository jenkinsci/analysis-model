package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link ArmccCompilerParser}.
 */
public class ArmccCompilerParserTest extends AbstractParserTest {
    private static final String WARNING_CATEGORY = DEFAULT_CATEGORY;

    /**
     * Creates a new instance of {@link AbstractParserTest}.
     *
     */
    protected ArmccCompilerParserTest(){
        super("armcc.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Issues<Issue> issues, final SoftAssertions softly) {

        softly.assertThat(issues).hasSize(3);

        softly.assertThat(issues.get(0)).hasPriority(Priority.HIGH)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(21)
                .hasLineEnd(21)
                .hasMessage("5 - cannot open source input file \"somefile.h\": No such file or directory")
                .hasFileName("/home/test/main.cpp");
        softly.assertThat(issues.get(1)).hasPriority(Priority.HIGH)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(23)
                .hasLineEnd(23)
                .hasMessage("5 - cannot open source input file \"somefile.h\": No such file or directory")
                .hasFileName("C:/home/test/main.cpp");
        softly.assertThat(issues.get(2)).hasPriority(Priority.NORMAL)
                .hasCategory(WARNING_CATEGORY)
                .hasLineStart(25)
                .hasLineEnd(25)
                .hasMessage("550 - something bad happened here")
                .hasFileName("/home/test/main.cpp");
    }

    @Override
    protected AbstractParser createParser() {
        return new ArmccCompilerParser();
    }
}

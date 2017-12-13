package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.AbstractParser;
import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.Issue;
import edu.hm.hafner.analysis.Issues;
import edu.hm.hafner.analysis.Priority;
import edu.hm.hafner.analysis.assertj.SoftAssertions;
import static edu.hm.hafner.analysis.assertj.IssuesAssert.*;

/**
 * Tests the class {@link InvalidsParser}.
 */
public class InvalidsParserTest extends AbstractParserTest {

    /**
     * Creates a new instance of {@link AbstractParserTest}.
     */
    protected InvalidsParserTest() {
        super("invalids.txt");
    }

    @Override
    protected void assertThatIssuesArePresent(final Issues<Issue> issues, final SoftAssertions softly) {
        Issues<Issue> warnings = createParser().parse(openFile());

        assertThat(warnings).hasSize(3);

        String type = "Oracle Invalid";

        softly.assertThat(warnings.get(0))
                .hasPriority(Priority.NORMAL)
                .hasCategory("PLW-05004")
                .hasLineStart(45)
                .hasLineEnd(45)
                .hasMessage("Encountered the symbol \"END\" when expecting one of the following:")
                .hasFileName("ENV_UTIL#.PACKAGE BODY")
                .hasType(type)
                .hasPackageName("E");


        softly.assertThat(warnings.get(1))
                .hasPriority(Priority.LOW)
                .hasCategory("PLW-07202")
                .hasLineStart(5)
                .hasLineEnd(5)
                .hasMessage("Encountered the symbol \"END\" when expecting one of the following:")
                .hasFileName("ENV_ABBR#B.TRIGGER")
                .hasType(type)
                .hasPackageName("E");

        softly.assertThat(warnings.get(2))
                .hasPriority(Priority.HIGH)
                .hasCategory("ORA-29521")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("referenced name javax/management/MBeanConstructorInfo could not be found")
                .hasFileName("/b77ce675_LoggerDynamicMBean.JAVA CLASS")
                .hasType(type)
                .hasPackageName("E");
    }

    @Override
    protected AbstractParser createParser() {
        return new InvalidsParser();
    }
}


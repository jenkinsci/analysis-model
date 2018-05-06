package edu.hm.hafner.analysis.parser;

import edu.hm.hafner.analysis.AbstractIssueParserTest;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Priority;
import static edu.hm.hafner.analysis.assertj.IssuesAssert.*;
import edu.hm.hafner.analysis.assertj.SoftAssertions;

/**
 * Tests the class {@link InvalidsParser}.
 */
class InvalidsParserTest extends AbstractIssueParserTest {
    InvalidsParserTest() {
        super("invalids.txt");
    }

    @Override
    protected InvalidsParser createParser() {
        return new InvalidsParser();
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        assertThat(report).hasSize(3);

        String type = "Oracle Invalid";
        softly.assertThat(report.get(0))
                .hasPriority(Priority.NORMAL)
                .hasCategory("PLW-05004")
                .hasLineStart(45)
                .hasLineEnd(45)
                .hasMessage("Encountered the symbol \"END\" when expecting one of the following:")
                .hasFileName("ENV_UTIL#.PACKAGE BODY")
                .hasType(type)
                .hasPackageName("E");

        softly.assertThat(report.get(1))
                .hasPriority(Priority.LOW)
                .hasCategory("PLW-07202")
                .hasLineStart(5)
                .hasLineEnd(5)
                .hasMessage("Encountered the symbol \"END\" when expecting one of the following:")
                .hasFileName("ENV_ABBR#B.TRIGGER")
                .hasType(type)
                .hasPackageName("E");

        softly.assertThat(report.get(2))
                .hasPriority(Priority.HIGH)
                .hasCategory("ORA-29521")
                .hasLineStart(0)
                .hasLineEnd(0)
                .hasMessage("referenced name javax/management/MBeanConstructorInfo could not be found")
                .hasFileName("/b77ce675_LoggerDynamicMBean.JAVA CLASS")
                .hasType(type)
                .hasPackageName("E");
    }
}
